package com.blibli.future.data.validation.validator;

import com.blibli.future.data.repository.EmployeeRepository;
import com.blibli.future.data.validation.EmployeeExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
public class EmployeeExistsByIdValidator implements ConstraintValidator<EmployeeExists, String> {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Override
  public boolean isValid(String id, ConstraintValidatorContext context) {
    if (!StringUtils.hasText(id)) {
      return true;
    }
    return employeeRepository.existsById(id);
  }

}
