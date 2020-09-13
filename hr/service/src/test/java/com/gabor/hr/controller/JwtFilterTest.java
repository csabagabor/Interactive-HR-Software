package com.gabor.hr.controller;

import com.gabor.model.config.Constants;
import com.gabor.model.model.RoleName;
import com.gabor.hr.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
public class JwtFilterTest extends BaseControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void validLogin() throws Exception {
        String tokenWithExpTime = createTokenForUser("user@gmail.com",
                RoleName.ROLE_USER,
                true);

        mvc.perform(MockMvcRequestBuilders.put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenWithExpTime))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ted"))
                .andExpect(jsonPath("$.lastName").value("Hunter"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"))
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"));
    }

    @Test
    public void expiredJwtToken() throws Exception {
        String tokenWithExpTime = createTokenWithExpTime("user@gmail.com",
                RoleName.ROLE_USER,
                new Date(System.currentTimeMillis() - 100));

        mvc.perform(MockMvcRequestBuilders.put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenWithExpTime))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void invalidUserInJwtToken() throws Exception {
        String tokenWithExpTime = createTokenForUser("invalid@gmail.com",
                RoleName.ROLE_USER,
                true);

        mvc.perform(MockMvcRequestBuilders.put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenWithExpTime))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void incorrectFormatJwtToken() throws Exception {
        String tokenWithExpTime = Jwts.builder()
                .setSubject("user@gmail.com")
                .claim(Constants.ROLE_KEY, RoleName.ROLE_USER.name())
                .signWith(SignatureAlgorithm.HS256, "invalidSecret")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100))
                .compact();


        mvc.perform(MockMvcRequestBuilders.put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenWithExpTime))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void incorrectSignature() throws Exception {
        String tokenWithExpTime = "blabla";

        mvc.perform(MockMvcRequestBuilders.put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenWithExpTime))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void invalidatedToken() throws Exception {
        //first create token, then update User so as to raise its tokens' minimum issue time
        String tokenWithExpTime = createTokenForUser("user@gmail.com",
                RoleName.ROLE_USER,
                true);

        //must wait a couple of millisecond
        Thread.sleep(10);

        userService.update(userService.findById(2L));//user@gmail.com is the second user

        mvc.perform(MockMvcRequestBuilders.put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenWithExpTime))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }
}
