package com.blibli.future.data.configuration;

import com.blibli.future.data.entity.Department;
import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.repository.DepartmentRepository;
import com.blibli.future.data.repository.EmployeeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Configuration
public class DataInitializationConfiguration implements InitializingBean {

  private final DepartmentRepository departmentRepository;

  private final EmployeeRepository employeeRepository;

  public DataInitializationConfiguration(DepartmentRepository departmentRepository,
                                         EmployeeRepository employeeRepository) {
    this.departmentRepository = departmentRepository;
    this.employeeRepository = employeeRepository;
  }

  @Override
  public void afterPropertiesSet() {
    initializeItDepartmentEmployeeData();
    initializeFinanceDepartmentEmployeeData();
    initializeOperationDepartmentEmployeeData();
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
