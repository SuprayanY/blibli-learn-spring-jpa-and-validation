package com.blibli.future.data.web.controller;

import com.blibli.future.data.entity.Department;
import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.repository.DepartmentRepository;
import com.blibli.future.data.repository.EmployeeRepository;
import com.blibli.future.data.web.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Api
@RestController
public class HelperController {

  private final DepartmentRepository departmentRepository;

  private final EmployeeRepository employeeRepository;

  public HelperController(DepartmentRepository departmentRepository,
                          EmployeeRepository employeeRepository) {
    this.departmentRepository = departmentRepository;
    this.employeeRepository = employeeRepository;
  }

  @ApiOperation("Populate employee data")
  @PostMapping(path = "/api/helper/employees/_populate",
               produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> populateEmployeeData() {
    initializeItDepartmentEmployeeData();
    initializeFinanceDepartmentEmployeeData();
    initializeOperationDepartmentEmployeeData();
    return Response.<Void>builder()
        .status(HttpStatus.OK.value())
        .build();
  }

  @ApiOperation("Clear employee data")
  @DeleteMapping(path = "/api/helper/employees/_clear",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> clearEmployeeData() {
    departmentRepository.deleteAll();
    employeeRepository.deleteAll();
    return Response.<Void>builder()
        .status(HttpStatus.OK.value())
        .build();
  }

  private void initializeItDepartmentEmployeeData () {
    Department department = departmentRepository.save(Department.builder()
        .name("IT")
        .build());
    employeeRepository.save(Employee.builder()
        .name("Mashuri Hasan")
        .birthdate(LocalDate.of(1990, Month.APRIL, 1))
        .department(department)
        .salary(BigInteger.valueOf(100_000_000L))
        .build());
    employeeRepository.save(Employee.builder()
        .name("Wilson")
        .birthdate(LocalDate.of(1995, Month.FEBRUARY, 21))
        .department(department)
        .salary(BigInteger.valueOf(101_000_000L))
        .build());
    employeeRepository.save(Employee.builder()
        .name("Suprayan Yapura")
        .birthdate(LocalDate.of(2000, Month.JUNE, 7))
        .department(department)
        .salary(BigInteger.valueOf(10_000_000L))
        .build());
  }

  private void initializeFinanceDepartmentEmployeeData () {
    Department department = departmentRepository.save(Department.builder()
        .name("Finance")
        .build());
    employeeRepository.save(Employee.builder()
        .name("Wahyudi")
        .birthdate(LocalDate.of(1992, Month.AUGUST, 19))
        .department(department)
        .salary(BigInteger.valueOf(120_000_000L))
        .build());
  }

  private void initializeOperationDepartmentEmployeeData () {
    Department department = departmentRepository.save(Department.builder()
        .name("Operation")
        .build());
    employeeRepository.save(Employee.builder()
        .name("Dika")
        .birthdate(LocalDate.of(1993, Month.DECEMBER, 3))
        .department(department)
        .salary(BigInteger.valueOf(110_000_000L))
        .build());
  }

}
