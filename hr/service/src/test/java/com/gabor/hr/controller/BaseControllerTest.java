package com.gabor.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabor.model.config.Constants;
import com.gabor.hr.config.TokenProvider;
import com.gabor.model.model.RoleName;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Value("${jwt.secret}")
    private String secret = "noteasy";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TokenProvider tokenProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

    protected String createTokenForUser(String subject, RoleName roleName, boolean rememberMe) {
        long expirationTime = Constants.ACCESS_TOKEN_VALIDITY_SECONDS;

        if (rememberMe) {
            expirationTime = Constants.ACCESS_TOKEN_VALIDITY_SECONDS_REMEMBER_ME;
        }

        return createTokenWithExpTime(subject, roleName,
                new Date(System.currentTimeMillis() + expirationTime * 1000));
    }

    protected String createTokenWithExpTime(String subject, RoleName roleName, Date expTime) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(Constants.ROLE_KEY, roleName.name())
                .signWith(SignatureAlgorithm.HS256, secret)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expTime)
                .compact();
    }
}
