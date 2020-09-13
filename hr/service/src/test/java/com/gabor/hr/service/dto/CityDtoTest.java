package com.gabor.hr.service.dto;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CityDtoTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void valid() {
        CityDto dto = new CityDto("city", "Germany");

        Set<ConstraintViolation<CityDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void invalidName() {
        CityDto dto = new CityDto("", "Germany");

        Set<ConstraintViolation<CityDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void invalidCountry() {
        CityDto dto = new CityDto("dasd", "");

        Set<ConstraintViolation<CityDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void invalidNameAndCountry() {
        CityDto dto = new CityDto("", "");

        Set<ConstraintViolation<CityDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }
}