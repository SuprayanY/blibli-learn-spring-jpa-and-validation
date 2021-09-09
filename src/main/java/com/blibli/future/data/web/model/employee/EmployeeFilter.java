package com.blibli.future.data.web.model.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFilter {

  private String name;

  private String department;

  @Min(value = 0, message = "Min_0")
  private Integer page = 0;

  @Min(value = 1, message = "Min_1")
  private Integer size = 5;

}
