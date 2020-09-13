package com.gabor.hr.service.dto.validator;

import com.gabor.hr.service.dto.RequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartAndEndDateValidator implements ConstraintValidator<EndDateAfterStartDate, RequestDto> {
    @Override
    public boolean isValid(RequestDto requestDto, ConstraintValidatorContext constraintValidatorContext) {

        if (requestDto.getEndDate() == null || requestDto.getStartDate() == null) {
            return false;
        }

        if (requestDto.getEndDate().isBefore(requestDto.getStartDate())) {
            return false;
        }

        return true;
    }
}
