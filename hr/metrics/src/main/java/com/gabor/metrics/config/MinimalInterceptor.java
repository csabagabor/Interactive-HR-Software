package com.gabor.metrics.config;

import com.gabor.LoginRequest;
import com.gabor.LoginResponse;
import com.gabor.LoginServiceGrpc;
import com.gabor.metrics.exception.BadCredentialsException;
import com.gabor.metrics.repository.MetricCountryRepository;
import com.gabor.model.model.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MinimalInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private LoginServiceGrpc.LoginServiceBlockingStub blockingStub;

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception {
        //modern browsers send pre-flight request
        if ("options".equals(requestServlet.getMethod().toLowerCase())) {
            return true;
        }

        final String authHeader = requestServlet.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing Authorization header.");
            throw new BadCredentialsException("Not authorized");
        } else {
            String authToken = authHeader.replace("Bearer ", "");
            final LoginResponse blockResponse = blockingStub.login(
                    LoginRequest.newBuilder()
                            .setToken(authToken)
                            .build());

            String role = blockResponse.getRole();
            if (!role.equals(RoleName.ROLE_MODERATOR.name())) {
                log.warn("Invalid role!");
                throw new BadCredentialsException("Access denied");
            }
        }


        return true;
    }

}