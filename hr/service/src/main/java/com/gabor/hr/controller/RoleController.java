package com.gabor.hr.controller;

import com.gabor.hr.model.Role;
import com.gabor.hr.service.RoleService;
import com.gabor.hr.service.dto.RoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/roles")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class RoleController extends CrudController<Role, RoleDto,
        RoleService> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }
}
