package com.blibli.future.data.repository;

import com.blibli.future.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
public interface EmployeeRepository extends JpaRepository<Employee, String>, EmployeeCustomRepository {

}
