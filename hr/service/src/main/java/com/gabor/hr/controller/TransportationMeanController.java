package com.gabor.hr.controller;

import com.gabor.hr.model.TransportationMean;
import com.gabor.hr.service.TransportationMeanService;
import com.gabor.hr.service.dto.TransportationMeanDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/transportation-means")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class TransportationMeanController extends CrudController<TransportationMean, TransportationMeanDto,
        TransportationMeanService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
