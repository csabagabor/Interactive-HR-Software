package com.gabor.hr.service.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface DateInFuture {
    String message() default "Date must not be in the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
