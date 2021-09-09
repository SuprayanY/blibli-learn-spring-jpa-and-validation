package com.blibli.future.data.web.controller;

import com.blibli.future.data.SpringDataApplication;
import com.blibli.future.data.entity.Department;
import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.repository.DepartmentRepository;
import com.blibli.future.data.repository.EmployeeRepository;
import com.blibli.future.data.web.model.Response;
import com.blibli.future.data.web.model.employee.CreateEmployeeRequest;
import com.blibli.future.data.web.model.employee.EmployeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@SpringBootTest(classes = SpringDataApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class EmployeeControllerTest {

  private static final String DEPARTMENT_NAME = "IT";
  private static final String EMPLOYEE_NAME = "Warren";
  private static final String EMPLOYEE_NEW_NAME = "Warren James";
  private static final LocalDate BIRTHDATE = LocalDate.of(1990, Month.MAY, 2);
  private static final BigInteger SALARY = BigInteger.valueOf(1_000_000);

  @Autowired
  private WebTestClient client;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll();
    departmentRepository.deleteAll();

    departmentRepository.save(Department.builder()
        .name(DEPARTMENT_NAME)
        .build());
  }

  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  @Test
  void test() {
    System.out.println(datasourceUrl);
  }

  @Test
  void createWithValidRequestThenEmployeeIsCreated() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Response<EmployeeResponse> response = client.post()
        .uri("/api/employees")
        .body(BodyInserters.fromValue(CreateEmployeeRequest.builder()
            .name(EMPLOYEE_NAME)
            .birthdate(BIRTHDATE)
            .departmentId(department.getId())
            .salary(SALARY)
            .build()))
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNull(response.getErrors());
    assertEquals(EMPLOYEE_NAME, response.getData().getName());
    assertEquals(BIRTHDATE, response.getData().getBirthdate());
    assertEquals(department.getId(), response.getData().getDepartment().getId());
    assertEquals(DEPARTMENT_NAME, response.getData().getDepartment().getName());
    assertEquals(SALARY, response.getData().getSalary());
    assertTrue(employeeRepository.existsById(response.getData().getId()));
  }

  @Test
  void createWithBadRequestThenEmployeeIsNotCreated() {
    Response<EmployeeResponse> response = client.post()
        .uri("/api/employees")
        .body(BodyInserters.fromValue(CreateEmployeeRequest.builder()
            .departmentId("invalid-department-id")
            .salary(BigInteger.valueOf(-1))
            .build()))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertNull(response.getData());
    assertTrue(response.getErrors().get("name").contains("Blank"));
    assertTrue(response.getErrors().get("birthdate").contains("Null"));
    assertTrue(response.getErrors().get("departmentId").contains("NotExist"));
    assertTrue(response.getErrors().get("salary").contains("Min_0"));
  }

  @Test
  void findAllWithValidRequestThenSuccess() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Department financeDepartment = departmentRepository.save(Department.builder()
        .name("finance")
        .build());

    List<Employee> employees = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      employees.add(generateEmployee("IT Employee " + i, department));
      employees.add(generateEmployee("Finance Employee " + i, financeDepartment));
    }
    employeeRepository.saveAll(employees);

    Response<List<EmployeeResponse>> response = client.get()
        .uri(uriBuilder -> uriBuilder.path("/api/employees/")
            .queryParam("page", 0)
            .queryParam("size", 2)
            .queryParam("department", "IT")
            .queryParam("name", "IT")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<Response<List<EmployeeResponse>>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNull(response.getErrors());

    Response.Pagination expectedPagination = Response.Pagination.builder()
        .page(0)
        .size(2L)
        .totalItems(10L)
        .build();
    assertEquals(expectedPagination, response.getPagination());
    assertEquals(2, response.getData().size());
    assertEquals("IT Employee 0", response.getData().get(0).getName());
    assertEquals("IT Employee 1", response.getData().get(1).getName());
  }

  @Test
  void findAllWithBadRequestThen400() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Response<List<EmployeeResponse>> response = client.get()
        .uri(uriBuilder -> uriBuilder.path("/api/employees/")
            .queryParam("page", -1)
            .queryParam("size", 0)
            .build())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(new ParameterizedTypeReference<Response<List<EmployeeResponse>>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertNull(response.getData());
    assertTrue(response.getErrors().get("page").contains("Min_0"));
    assertTrue(response.getErrors().get("size").contains("Min_1"));

  }

  Employee generateEmployee(String name, Department department) {
    return Employee.builder()
        .name(name)
        .birthdate(BIRTHDATE)
        .department(department)
        .salary(SALARY)
        .build();
  }

  @Test
  void findByIdWithExistingEmployeeIdThenSuccess() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Employee employee = employeeRepository.save(Employee.builder()
        .name(EMPLOYEE_NAME)
        .birthdate(BIRTHDATE)
        .department(department)
        .salary(SALARY)
        .build());
    Response<EmployeeResponse> response = client.get()
        .uri("/api/employees/{id}", employee.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNull(response.getErrors());
    assertEquals(EMPLOYEE_NAME, response.getData().getName());
    assertEquals(BIRTHDATE, response.getData().getBirthdate());
    assertEquals(department.getId(), response.getData().getDepartment().getId());
    assertEquals(DEPARTMENT_NAME, response.getData().getDepartment().getName());
    assertEquals(SALARY, response.getData().getSalary());
  }

  @Test
  void findByIdWithNonExistentEmployeeIdThen400() {
    Response<EmployeeResponse> response = client.get()
        .uri("/api/employees/{id}", "nonexistent-id")
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertNull(response.getData());
    assertTrue(response.getErrors().get("findById.id").contains("NotExist"));
  }

  @Test
  void updateWithValidRequestThenUpdated() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Employee employee = employeeRepository.save(Employee.builder()
        .name(EMPLOYEE_NAME)
        .birthdate(BIRTHDATE)
        .department(department)
        .salary(SALARY)
        .build());
    Response<EmployeeResponse> response = client.put()
        .uri("/api/employees/{id}", employee.getId())
        .body(BodyInserters.fromValue(CreateEmployeeRequest.builder()
            .name(EMPLOYEE_NEW_NAME)
            .birthdate(BIRTHDATE)
            .departmentId(department.getId())
            .salary(SALARY)
            .build()))
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNull(response.getErrors());
    assertEquals(EMPLOYEE_NEW_NAME, response.getData().getName());
    assertEquals(BIRTHDATE, response.getData().getBirthdate());
    assertEquals(department.getId(), response.getData().getDepartment().getId());
    assertEquals(DEPARTMENT_NAME, response.getData().getDepartment().getName());
    assertEquals(SALARY, response.getData().getSalary());
  }

  @Test
  void updateWithNonExistentIdThen400() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Response<EmployeeResponse> response = client.put()
        .uri("/api/employees/{id}", "nonexistent-id")
        .body(BodyInserters.fromValue(CreateEmployeeRequest.builder()
            .name(EMPLOYEE_NEW_NAME)
            .birthdate(BIRTHDATE)
            .departmentId(department.getId())
            .salary(SALARY)
            .build()))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertNull(response.getData());
    assertTrue(response.getErrors().get("update.id").contains("NotExist"));
  }

  @Test
  void deleteWithValidRequestThenDeleted() {
    Department department = departmentRepository.findByName(DEPARTMENT_NAME);
    Employee employee = employeeRepository.save(Employee.builder()
        .name(EMPLOYEE_NAME)
        .birthdate(BIRTHDATE)
        .department(department)
        .salary(SALARY)
        .build());
    Response<EmployeeResponse> response = client.delete()
        .uri("/api/employees/{id}", employee.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNull(response.getData());
    assertNull(response.getErrors());
  }

  @Test
  void deleteWithNonExistentIdThen400() {
    Response<EmployeeResponse> response = client.delete()
        .uri("/api/employees/{id}", "nonexistent-id")
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(new ParameterizedTypeReference<Response<EmployeeResponse>>() {})
        .returnResult()
        .getResponseBody();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertNull(response.getData());
    assertTrue(response.getErrors().get("delete.id").contains("NotExist"));
  }

}
