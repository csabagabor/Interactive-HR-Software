package com.gabor.hr.controller;

import com.gabor.hr.service.dto.special.AuthTokenDto;
import com.gabor.hr.service.dto.special.LoginUserDto;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Transactional
public class AuthenticationControllerTest extends BaseControllerTest {
    @Test
    public void loginUserRememberMe() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("user@gmail.com", "user", true);

        MvcResult result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthTokenDto authTokenDto = objectMapper.readValue(content, AuthTokenDto.class);


        assertEquals("user@gmail.com", tokenProvider.getEmailFromToken(authTokenDto.getToken()));
        assertEquals("ROLE_USER", tokenProvider.getRoleFromToken(authTokenDto.getToken()));
        Date expirationDateFromToken = tokenProvider.getClaimsFromToken(authTokenDto.getToken()).getExpiration();
        Date now = new Date();
        long diffInMillies = Math.abs(expirationDateFromToken.getTime() - now.getTime());

        assertTrue(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) >= 29);
    }

    @Test
    public void loginUserWithoutRememberMe() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("user@gmail.com", "user", false);

        MvcResult result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthTokenDto authTokenDto = objectMapper.readValue(content, AuthTokenDto.class);

        assertEquals("user@gmail.com", tokenProvider.getEmailFromToken(authTokenDto.getToken()));
        assertEquals("ROLE_USER", tokenProvider.getRoleFromToken(authTokenDto.getToken()));
        Date expirationDateFromToken = tokenProvider.getClaimsFromToken(authTokenDto.getToken()).getExpiration();
        Date now = new Date();
        long diffInMillies = Math.abs(expirationDateFromToken.getTime() - now.getTime());

        assertTrue(TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS) >= 4);
    }

    @Test
    public void loginInvalidPassword() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("user@gmail.com", "invalid", false);

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }
}