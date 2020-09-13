package com.gabor.hr.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtErrorHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception instanceof ExpiredJwtException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token Expired");
        } else if (exception != null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Cannot parse token");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: other cause");
        }
    }
}