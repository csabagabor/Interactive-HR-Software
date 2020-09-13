package com.gabor.hr.service.dto.validator;

import com.gabor.hr.model.Status;
import com.gabor.hr.service.dto.RequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<DateInFuture, RequestDto> {
    @Override
    public boolean isValid(RequestDto requestDto, ConstraintValidatorContext constraintValidatorContext) {
        //date must be in the future only for newly created requests
        if (requestDto.getStatus() == Status.OPEN) {
            LocalDate now = LocalDate.now();

            if (requestDto.getEndDate() == null || requestDto.getStartDate() == null) {
                return false;
            }

            if (requestDto.getEndDate().isBefore(now)) {
                return false;
            }

            if (requestDto.getStartDate().isBefore(now)) {
                return false;
            }
        }

        return true;
    }
}
