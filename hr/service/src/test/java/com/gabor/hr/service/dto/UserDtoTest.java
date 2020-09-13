package com.gabor.hr.service.dto;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class UserDtoTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void valid() {
        UserDto dto = new UserDto("firstName", "lastName", "email@gmail.com", "ROLE_ADMIN");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void invalidFirstName() {
        UserDto dto = new UserDto("", "lastName", "email@gmail.com", "ROLE_ADMIN");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void invalidLastName() {
        UserDto dto = new UserDto("dsada", "", "email@gmail.com", "ROLE_ADMIN");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void invalidEmptyEmail() {
        UserDto dto = new UserDto("dsada", "gdsgg", "", "ROLE_ADMIN");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void invalidIncorrectEmail() {
        UserDto dto = new UserDto("dsada", "gdsgg", "email", "ROLE_ADMIN");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void invalidRole() {
        UserDto dto = new UserDto("dsada", "gdsgg", "email@gmail.com", "");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertTrue(violations.size() >= 1);
    }
}