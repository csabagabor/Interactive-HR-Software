package com.gabor.hr.service.dto;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class CountryDtoTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void valid() {
        CountryDto dto = new CountryDto("Country");

        Set<ConstraintViolation<CountryDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void invalidName() {
        CountryDto dto = new CountryDto("");

        Set<ConstraintViolation<CountryDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}