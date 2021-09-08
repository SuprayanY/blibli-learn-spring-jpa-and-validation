package com.blibli.future.data.web.controller;

import com.blibli.future.data.entity.Department;
import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.service.EmployeeService;
import com.blibli.future.data.validation.EmployeeExists;
import com.blibli.future.data.web.model.Response;
import com.blibli.future.data.web.model.department.DepartmentResponse;
import com.blibli.future.data.web.model.employee.CreateEmployeeRequest;
import com.blibli.future.data.web.model.employee.EmployeeFilter;
import com.blibli.future.data.web.model.employee.EmployeeResponse;
import com.blibli.future.data.web.model.employee.UpdateEmployeeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Api
@Validated
@RestController
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @ApiOperation("Create an employee")
  @PostMapping(path = "/api/employees",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<EmployeeResponse> create(@Valid @RequestBody CreateEmployeeRequest request) {
    Employee newEmployee = employeeService.create(request);
    EmployeeResponse employeeResponse = convertToResponse(newEmployee);
    return Response.<EmployeeResponse>builder()
        .status(HttpStatus.OK.value())
        .data(employeeResponse)
        .build();
  }

  @ApiOperation("Find all employees")
  @GetMapping(path = "/api/employees",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<List<EmployeeResponse>> findAll(@Valid EmployeeFilter employeeFilter) {
    Page<Employee> employeePage = employeeService.findAll(employeeFilter);
    List<EmployeeResponse> employeeResponses = employeePage.map(this::convertToResponse)
        .getContent();
    Response.Pagination pagination = convertToResponse(employeePage);
    return Response.<List<EmployeeResponse>>builder()
        .status(HttpStatus.OK.value())
        .data(employeeResponses)
        .pagination(pagination)
        .build();
  }

  @ApiOperation("Find an employee")
  @GetMapping(path = "/api/employees/{id}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<EmployeeResponse> findById(@EmployeeExists @PathVariable String id) {
    Employee employee = employeeService.findById(id);
    EmployeeResponse employeeResponse = convertToResponse(employee);
    return Response.<EmployeeResponse>builder()
        .status(HttpStatus.OK.value())
        .data(employeeResponse)
        .build();
  }

  @ApiOperation("Update an employee")
  @PutMapping(path = "/api/employees/{id}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<EmployeeResponse> update(@EmployeeExists @PathVariable String id,
                                           @Valid @RequestBody UpdateEmployeeRequest request) {
    Employee employee = employeeService.update(id, request);
    EmployeeResponse employeeResponse = convertToResponse(employee);
    return Response.<EmployeeResponse>builder()
        .status(HttpStatus.OK.value())
        .data(employeeResponse)
        .build();
  }

  @ApiOperation("Delete an employee")
  @DeleteMapping(path = "/api/employees/{id}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> delete(@EmployeeExists @PathVariable String id) {
    employeeService.delete(id);
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

  private Response.Pagination convertToResponse(Page<?> page) {
    return Response.Pagination.builder()
        .page(page.getNumber())
        .size((long) page.getSize())
        .totalItems(page.getTotalElements())
        .build();
  }

}
