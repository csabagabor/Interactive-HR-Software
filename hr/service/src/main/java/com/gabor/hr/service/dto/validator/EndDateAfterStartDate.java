package com.gabor.hr.service.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartAndEndDateValidator.class)
@Documented
public @interface EndDateAfterStartDate {
    String message() default "end Date must be after start Date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
