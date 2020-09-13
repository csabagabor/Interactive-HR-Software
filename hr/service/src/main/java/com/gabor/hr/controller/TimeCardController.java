package com.gabor.hr.controller;

import com.gabor.hr.exception.ResourceNotFoundException;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.TimeCard;
import com.gabor.hr.service.TimecardService;
import com.gabor.hr.service.dto.RequestDto;
import com.gabor.hr.service.dto.TimeCardDto;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@RequestMapping("/api/timecards")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class TimeCardController extends CrudController<TimeCard, TimeCardDto,
        TimecardService> {
    @Autowired
    private HttpServletRequest httpServletRequest;


    @GetMapping("/status")
    public ResponseEntity<?> findStatus() {

        Optional<TimeCardDto> currentTimeCard = service.getCurrentTimeCard(getCurrentUserEmail());

        if (!currentTimeCard.isPresent()) {
            throw new ResourceNotFoundException("timecard", "year", "current");
        }

        return new ResponseEntity<>(currentTimeCard.get().getStatus(), HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody JsonObject hidden) {

        if (hidden != null && hidden.has("hidden") && "hidden".equals(hidden.get("hidden"))) {//only used for interactive tutorial
            return new ResponseEntity<>(HttpStatus.OK);
        }

        Optional<TimeCardDto> currentTimeCard = service.getCurrentTimeCard(getCurrentUserEmail());

        if (!currentTimeCard.isPresent()) {
            throw new ResourceNotFoundException("timecard", "year", "current");
        }

        TimeCardDto currentTimeCardDto = currentTimeCard.get();
        TimeCardDto timeCardDto = service.updateWithTasks(currentTimeCardDto);

        return new ResponseEntity<>(timeCardDto, HttpStatus.OK);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        service.accept(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        service.reject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/open")
    public ResponseEntity<?> findAllOpen() {
        return new ResponseEntity<>(service.findAllByStatus(Status.OPEN), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> findAllTasks(@PathVariable Long id) {
        return new ResponseEntity<>(service.findTasksById(id), HttpStatus.OK);
    }

    @GetMapping("/tasks-payroll/{id}")
    public ResponseEntity<?> findAllTasksPayroll(@PathVariable Long id) {
        return new ResponseEntity<>(service.findTasksByIdPayroll(id), HttpStatus.OK);
    }

    @GetMapping("/past-month")
    public ResponseEntity<?> findAllPastMonth() {
        return new ResponseEntity<>(service.findAllPastMonth(), HttpStatus.OK);
    }

    @GetMapping("/accepted")
    public ResponseEntity<?> findAllAccepted() {
        return new ResponseEntity<>(service.findAllByStatus(Status.ACCEPTED), HttpStatus.OK);
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
