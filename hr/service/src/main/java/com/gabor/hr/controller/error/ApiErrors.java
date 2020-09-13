package com.gabor.hr.controller.error;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiErrors {

    private List<String> message = new ArrayList<>();

    public ApiErrors(Errors errors) {
        List<ObjectError> errorList = errors.getAllErrors();
        for (ObjectError e : errorList) {
            message.add(e.getDefaultMessage());
        }
    }
}
