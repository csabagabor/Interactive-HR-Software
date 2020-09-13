package com.gabor.hr.controller;

import com.gabor.hr.model.Country;
import com.gabor.hr.service.CountryService;
import com.gabor.hr.service.dto.CountryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/countries")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CountryController extends CrudController<Country, CountryDto,
        CountryService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
