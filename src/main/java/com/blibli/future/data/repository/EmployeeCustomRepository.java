package com.blibli.future.data.repository;

import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.web.model.employee.EmployeeFilter;
import org.springframework.data.domain.Page;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
public interface EmployeeCustomRepository {

  Page<Employee> findByFilter(EmployeeFilter filter);

}
