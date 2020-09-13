package com.gabor.hr.controller;

import com.gabor.hr.service.MessageService;
import com.gabor.model.dto.CommandModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/commands")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CommandController extends BaseController {

    @Autowired
    private MessageService messageService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/business-request")
    public ResponseEntity<?> businessRequest() {
        messageService.sendCommand(new CommandModel("command:business-trip", getCurrentUserEmail()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/timecard-fill")
    public ResponseEntity<?> timecardFill() {
        messageService.sendCommand(new CommandModel("command:timecard-fill", getCurrentUserEmail()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
