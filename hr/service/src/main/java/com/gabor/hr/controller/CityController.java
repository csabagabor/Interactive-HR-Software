package com.gabor.hr.controller;

import com.gabor.hr.model.City;
import com.gabor.hr.service.CityService;
import com.gabor.hr.service.dto.CityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/cities")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CityController extends CrudController<City, CityDto,
        CityService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<?> findAllByCountry(@PathVariable String country) {
        return new ResponseEntity<>(service.findAllByCountry(country), HttpStatus.OK);
    }
}
