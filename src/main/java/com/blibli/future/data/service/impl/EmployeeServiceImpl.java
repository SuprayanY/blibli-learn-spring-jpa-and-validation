package com.blibli.future.data.service.impl;

import com.blibli.future.data.entity.Department;
import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.repository.DepartmentRepository;
import com.blibli.future.data.repository.EmployeeRepository;
import com.blibli.future.data.service.EmployeeService;
import com.blibli.future.data.web.model.employee.CreateEmployeeRequest;
import com.blibli.future.data.web.model.employee.EmployeeFilter;
import com.blibli.future.data.web.model.employee.UpdateEmployeeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final DepartmentRepository departmentRepository;

  private final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository) {
    this.departmentRepository = departmentRepository;
    this.employeeRepository = employeeRepository;
  }

  @Override
  // rollback transaction should Exception.class (or any subclass) is thrown
  @Transactional(rollbackFor = { Exception.class })
  public Employee create(CreateEmployeeRequest request, boolean throwException) {
    Department department = departmentRepository.getById(request.getDepartmentId());
    Employee newEmployee = Employee.builder()
        .department(department)
        .build();
    BeanUtils.copyProperties(request, newEmployee);
    newEmployee = employeeRepository.save(newEmployee);
    if (throwException) {
      log.info("Employee should not be committed into database: {}", newEmployee);
      throw new RuntimeException("Exception to try @Transactional annotation.");
    }
    return newEmployee;
  }

  @Override
  public Page<Employee> findAll(EmployeeFilter filter) {
    return employeeRepository.findByFilter(filter);
  }

  @Override
  public Employee findById(String id) {
    return employeeRepository.getById(id);
  }

  @Override
  public Employee update(String id, UpdateEmployeeRequest request) {
    Employee employee = employeeRepository.getById(id);
    BeanUtils.copyProperties(request, employee);
    if (!request.getDepartmentId().equals(employee.getDepartment().getId())) {
      Department department = departmentRepository.getById(request.getDepartmentId());
      employee.setDepartment(department);
    }
    return employeeRepository.save(employee);
  }

  @Override
  public void delete(String id) {
    employeeRepository.deleteById(id);
  }

}
