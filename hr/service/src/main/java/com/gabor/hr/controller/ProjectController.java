package com.gabor.hr.controller;

import com.gabor.hr.model.Project;
import com.gabor.hr.model.Status;
import com.gabor.hr.service.ProjectService;
import com.gabor.hr.service.TaskService;
import com.gabor.hr.service.dto.ProjectDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/projects")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ProjectController extends CrudController<Project, ProjectDto,
        ProjectService> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> findAllTasksByProjectId(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.findTasksByProject(id), HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> findAllTasksByProjectTitle(@RequestParam String title) {
        return new ResponseEntity<>(taskService.findTasksByProject(title), HttpStatus.OK);
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
