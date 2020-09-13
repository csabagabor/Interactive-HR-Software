package com.gabor.hr.service.dto;

import com.gabor.hr.model.Status;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class RequestModelTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void valid() {
        var dto = new RequestDto(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                false, false, true,
                "details",
                "car", "Romania", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void invalidEndDateBeforeStartDate() {
        var dto = new RequestDto(LocalDate.now().plusDays(22),
                LocalDate.now().plusDays(1),
                false, false, true,
                "details",
                "car", "Romania", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void invalidEndDateInPast() {
        var dto = new RequestDto(LocalDate.now().plusDays(1),
                LocalDate.now().minusMonths(1),
                false, false, true,
                "details",
                "car", "Romania", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void invalidStartDateInPast() {
        var dto = new RequestDto(LocalDate.now().minusMonths(1),
                LocalDate.now().plusDays(1),
                false, false, true,
                "details",
                "car", "Romania", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void invalidBlankTransportationMean() {
        var dto = new RequestDto(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                false, false, true,
                "details",
                "", "Romania", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void invalidBlankCountry() {
        var dto = new RequestDto(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                false, false, true,
                "details",
                "car", "", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void invalidBlankCity() {
        var dto = new RequestDto(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                false, false, true,
                "details",
                "car", "Romania", "",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }

}