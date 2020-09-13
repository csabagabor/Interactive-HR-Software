package com.gabor.metrics.controller;

import com.gabor.metrics.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class MetricsController {

    @Autowired
    private MetricsService requestService;

    @GetMapping(value = "/requests-per-month")
    public ResponseEntity<?> getMetricsWithRequestsPerMonth() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerMonth(), HttpStatus.OK);
    }

    @GetMapping(value = "/requests-per-country")
    public ResponseEntity<?> getMetricsWithRequestsPerCountry() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerCountry(), HttpStatus.OK);
    }

    @GetMapping(value = "/requests-per-transportation-mean")
    public ResponseEntity<?> getMetricsWithRequestsPerTransportationMean() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerTransportationMean(), HttpStatus.OK);
    }

    @GetMapping(value = "/requests-per-status")
    public ResponseEntity<?> getMetricsWithRequestsPerStatus() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerStatus(), HttpStatus.OK);
    }

    @GetMapping(value = "/requests-per-laptop")
    public ResponseEntity<?> getMetricsWithRequestsPerLaptop() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerLaptop(), HttpStatus.OK);
    }

    @GetMapping(value = "/requests-per-city")
    public ResponseEntity<?> getMetricsWithRequestsPerCity() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerCity(), HttpStatus.OK);
    }

    @GetMapping(value = "/requests-per-duration")
    public ResponseEntity<?> getMetricsWithRequestsPerDuration() {
        return new ResponseEntity<>(requestService.getMetricsWithRequestsPerDuration(), HttpStatus.OK);
    }
}
