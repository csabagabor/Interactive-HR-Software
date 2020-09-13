package com.gabor.hr.config;

import com.gabor.hr.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final TokenProvider jwtTokenUtil;


    public JwtFilter(UserService userService,
                     TokenProvider jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String authHeader = req.getHeader("Authorization");

        log.info("New Request");

        if (authHeader == null) {
            log.warn("Missing Authorization header");
        } else if (!authHeader.startsWith("Bearer ")) {
            log.warn("Not a Bearer header");
        } else {
            String email = null;
            String authToken = authHeader.replace("Bearer ", "");

            try {
                email = jwtTokenUtil.getEmailFromToken(authToken);
            } catch (ExpiredJwtException e) {
                log.warn("JWT token is expired", e);
                req.setAttribute("exception", e);
            } catch (Exception e) {
                log.error("Error verifying token", e);
                req.setAttribute("exception", e);
            }

            if (email != null) {

                //users can be deleted(or edited) and just checking the validity of the token is not enough
                //so a database call is a must to verify the user
                try {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userService
                            .findByEmail(email).getRoleName());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            email,
                            "",
                            Collections.singletonList(simpleGrantedAuthority)
                    );

                    //check token issued time
                    try {
                        userService.verifyTokenIssuedTime(email, jwtTokenUtil.getIssuedDateFromToken(authToken));

                        log.info("new request from user: {}", email);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } catch (Exception e) {
                        log.error("token issued time is lower than the minimum", e);
                        req.setAttribute("exception", e);
                    }

                } catch (Exception e) {
                    log.error("cannot verify user", e);
                    req.setAttribute("exception", e);
                }
            }
        }

        chain.doFilter(req, res);
    }
}