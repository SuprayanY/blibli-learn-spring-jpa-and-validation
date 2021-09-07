package com.blibli.future.data.web.controller;

import com.blibli.future.data.entity.Department;
import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.repository.DepartmentRepository;
import com.blibli.future.data.repository.EmployeeRepository;
import com.blibli.future.data.vdalidation.EmployeeExists;
import com.blibli.future.data.web.model.Response;
import com.blibli.future.data.web.model.department.DepartmentResponse;
import com.blibli.future.data.web.model.employee.CreateEmployeeRequest;
import com.blibli.future.data.web.model.employee.EmployeeResponse;
import com.blibli.future.data.web.model.employee.UpdateEmployeeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Api
@RestController
public class EmployeeController {

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @ApiOperation("Create an employee")
  @PostMapping(path = "/api/employees",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<EmployeeResponse> create(@Valid @RequestBody CreateEmployeeRequest request) {
    Department department = departmentRepository.getById(request.getDepartmentId());
    Employee newEmployee = Employee.builder()
        .department(department)
        .build();
    BeanUtils.copyProperties(request, newEmployee);
    newEmployee = employeeRepository.save(newEmployee);
    EmployeeResponse employeeResponse = convertToResponse(newEmployee);
    return Response.<EmployeeResponse>builder()
        .data(employeeResponse)
        .build();
  }

  @ApiOperation("Find all employees")
  @GetMapping(path = "/api/employees",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<List<EmployeeResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false) String name) {
    Pageable pageable = PageRequest.of(page, size);
    Employee example = new Employee();
    if (StringUtils.hasText(name)) {
      example.setName(name);
    }
    Page<Employee> employeePage = employeeRepository.findAll(Example.of(example), pageable);
    List<EmployeeResponse> employeeResponses = employeePage.map(this::convertToResponse).getContent();
    return Response.<List<EmployeeResponse>>builder()
        .data(employeeResponses)
        .pagination(convertToPagination(employeePage))
        .build();
  }

  @ApiOperation("Find an employee")
  @Validated
  @GetMapping(path = "/api/employees/{id}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<EmployeeResponse> findById(@EmployeeExists @PathVariable String id) {
    Employee employee = employeeRepository.getById(id);
    return Response.<EmployeeResponse>builder()
        .data(convertToResponse(employee))
        .build();
  }

  @ApiOperation("Update an employee")
  @PutMapping(path = "/api/employees/{id}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<EmployeeResponse> update(@EmployeeExists @PathVariable String id,
                                           @Valid @RequestBody UpdateEmployeeRequest request) {
    Employee employee = employeeRepository.getById(id);
    BeanUtils.copyProperties(request, employee);
    if (!request.getDepartmentId().equals(employee.getDepartment().getId())) {
      Department department = departmentRepository.getById(request.getDepartmentId());
      employee.setDepartment(department);
    }
    employee = employeeRepository.save(employee);
    return Response.<EmployeeResponse>builder()
        .data(convertToResponse(employee))
        .build();
  }

  @ApiOperation("Delete an employee")
  @DeleteMapping(path = "/api/employees/{id}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> delete(@EmployeeExists @PathVariable String id) {
    employeeRepository.deleteById(id);
    return Response.<Void>builder()
        .status(HttpStatus.OK.value())
        .build();
  }

  private EmployeeResponse convertToResponse(Employee employee) {
    EmployeeResponse employeeResponse = EmployeeResponse.builder()
        .department(convertToResponse(employee.getDepartment()))
        .build();
    BeanUtils.copyProperties(employee, employeeResponse);
    return employeeResponse;
  }

  private DepartmentResponse convertToResponse(Department department) {
    DepartmentResponse departmentResponse = DepartmentResponse.builder()
        .build();
    BeanUtils.copyProperties(department, departmentResponse);
    return departmentResponse;
  }

  private Response.Pagination convertToPagination(Page<?> page) {
    return Response.Pagination.builder()
        .page(page.getNumber())
        .size((long) page.getSize())
        .totalItems(page.getTotalElements())
        .build();
  }

}
