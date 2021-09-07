package com.blibli.future.data.web.model.employee;

import com.blibli.future.data.web.model.department.DepartmentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

  private String id;

  private String name;

  private LocalDate birthdate;

  private DepartmentResponse department;

  private BigInteger salary;

}
