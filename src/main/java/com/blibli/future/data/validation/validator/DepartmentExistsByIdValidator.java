package com.blibli.future.data.validation.validator;

import com.blibli.future.data.repository.DepartmentRepository;
import com.blibli.future.data.validation.DepartmentExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
public class DepartmentExistsByIdValidator implements ConstraintValidator<DepartmentExists, String> {

  @Autowired
  private DepartmentRepository departmentRepository;

  @Override
  public boolean isValid(String id, ConstraintValidatorContext context) {
    // Don't validate outside of context. Blank validation should be done by related validator.
    if (!StringUtils.hasText(id)) {
      return true;
    }
    return departmentRepository.existsById(id);
  }

}
