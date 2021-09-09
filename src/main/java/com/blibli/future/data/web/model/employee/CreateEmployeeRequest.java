package com.blibli.future.data.web.model.employee;

import com.blibli.future.data.validation.DepartmentExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CreateEmployeeRequest {

  @NotBlank(message = "Blank")
  @Length(min = 2, max = 100, message = "Length_2_100")
  private String name;

  @NotNull(message = "Null")
  private LocalDate birthdate;

  @NotBlank(message = "Blank")
  @DepartmentExists
  private String departmentId;

  @Min(value = 0, message = "Min_0")
  private BigInteger salary;

}
