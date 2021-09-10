package com.blibli.future.data.service;

import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.web.model.employee.CreateEmployeeRequest;
import com.blibli.future.data.web.model.employee.EmployeeFilter;
import com.blibli.future.data.web.model.employee.UpdateEmployeeRequest;
import org.springframework.data.domain.Page;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
public interface EmployeeService {

  Employee create(CreateEmployeeRequest request, boolean throwException);

  Page<Employee> findAll(EmployeeFilter filter);

  Employee findById(String id);

  Employee update(String id, UpdateEmployeeRequest request);

  void delete(String id);

}
