package com.gabor.hr.controller;

import com.gabor.hr.config.TokenProvider;
import com.gabor.hr.controller.error.ApiErrors;
import com.gabor.hr.service.UserService;
import com.gabor.hr.service.dto.UserDto;
import com.gabor.hr.service.dto.special.PasswordDto;
import com.gabor.hr.service.dto.special.UserViewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AccountController extends BaseController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PutMapping(value = "/userinfo")
    public ResponseEntity<?> getUserInfo() {
        return new ResponseEntity<>(userService.findByEmail(getCurrentUserEmail()), HttpStatus.OK);
    }

    @PutMapping(value = "/changeUser")
    public ResponseEntity<?> updateData(@Valid @RequestBody UserDto userDto,
                                        BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ApiErrors(bindingResult), HttpStatus.BAD_REQUEST);
        } else {
            UserViewDto byEmail = userService.findByEmail(getCurrentUserEmail());

            //user can modify only its own user profile
            //that's why the id parameter cannot be sent from the frontend(for security purposes)
            userDto.setId(byEmail.getId());

            //also, role cannot be changed
            userDto.setRoleName(byEmail.getRoleName());
            return new ResponseEntity<>(userService.update(userDto), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDto passwordDto,
                                            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ApiErrors(bindingResult), HttpStatus.BAD_REQUEST);
        } else {
            userService.changePassword(getCurrentUserEmail(),
                    passwordDto.getOldPassword(),
                    passwordDto.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
