package com.gabor.hr.controller;


import com.gabor.hr.model.User;
import com.gabor.hr.service.UserService;
import com.gabor.hr.service.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UserController extends CrudController<User, UserDto,
        UserService> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public HttpServletRequest getHttpRequest() {
        return httpServletRequest;
    }

}
