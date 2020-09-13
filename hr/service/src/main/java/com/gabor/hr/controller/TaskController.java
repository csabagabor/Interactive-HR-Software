package com.gabor.hr.controller;

import com.gabor.hr.model.Task;
import com.gabor.hr.service.TaskService;
import com.gabor.hr.service.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/tasks")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class TaskController extends CrudController<Task, TaskDto,
        TaskService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
