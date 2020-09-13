package com.gabor.hr.controller;

import com.gabor.hr.model.WorkedTask;
import com.gabor.hr.service.WorkedTaskService;
import com.gabor.hr.service.dto.WorkedTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/worked-tasks")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class WorkedTaskController extends CrudController<WorkedTask, WorkedTaskDto,
        WorkedTaskService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/own")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAllOwn(getCurrentUserEmail()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> save(@Valid @RequestBody WorkedTaskDto obj, BindingResult bindingResult) {
        String currentUserEmail = getCurrentUserEmail();
        if ("hidden".equals(obj.getHidden())) { //only used for interactive tutorial
            return new ResponseEntity<>(HttpStatus.OK);
        }

        obj.setUserEmail(currentUserEmail);
        return super.save(obj, bindingResult);
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
