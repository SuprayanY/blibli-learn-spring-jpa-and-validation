package com.blibli.future.data.validation;

import com.blibli.future.data.validation.validator.DepartmentExistsByIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Documented
@Constraint(validatedBy = {
    DepartmentExistsByIdValidator.class
})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DepartmentExists {

  String message() default "NotExist";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
