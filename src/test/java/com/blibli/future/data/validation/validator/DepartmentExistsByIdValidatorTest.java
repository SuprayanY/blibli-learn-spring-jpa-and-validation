package com.blibli.future.data.validation.validator;

import com.blibli.future.data.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@ExtendWith(MockitoExtension.class)
class DepartmentExistsByIdValidatorTest {

  private static final String ID = "id";

  @Mock
  private DepartmentRepository departmentRepository;

  @InjectMocks
  private DepartmentExistsByIdValidator validator;

  @Test
  void isValid_idIsBlank_true() {
    assertTrue(validator.isValid(null, null));
    assertTrue(validator.isValid("", null));
    assertTrue(validator.isValid(" ", null));
    assertTrue(validator.isValid("\t", null));
    assertTrue(validator.isValid("\n", null));
  }

  @Test
  void isValid_departmentExists_true() {
    when(departmentRepository.existsById(ID)).thenReturn(true);
    assertTrue(validator.isValid(ID, null));
  }

  @Test
  void isValid_departmentDoestNotExist_true() {
    when(departmentRepository.existsById(ID)).thenReturn(false);
    assertFalse(validator.isValid(ID, null));
  }

}
