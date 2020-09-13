package com.gabor.hr.controller;

import com.gabor.model.model.RoleName;
import com.gabor.hr.service.UserService;
import com.gabor.hr.service.dto.UserDto;
import com.gabor.hr.service.dto.special.PasswordDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Transactional
public class AccountControllerTest extends BaseControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getUserInfo() throws Exception {
        mvc.perform(put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ted"))
                .andExpect(jsonPath("$.lastName").value("Hunter"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"))
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"));
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void getUserInfoAdmin() throws Exception {
        mvc.perform(put("/api/account/userinfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Lincoln"))
                .andExpect(jsonPath("$.lastName").value("Big"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.roleName").value("ROLE_ADMIN"));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateData() throws Exception {
        UserDto userDto = new UserDto("changed1", "changed2",
                "changed@gmail.com",
                RoleName.ROLE_USER.name());

        mvc.perform(put("/api/account/changeUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("changed1"))
                .andExpect(jsonPath("$.lastName").value("changed2"))
                .andExpect(jsonPath("$.email").value("changed@gmail.com"))
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateDataBadRequest() throws Exception {
        UserDto userDto = new UserDto("changed1", "changed2",
                "",
                RoleName.ROLE_USER.name());

        mvc.perform(put("/api/account/changeUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateDataCannotChangeRole() throws Exception {
        UserDto userDto = new UserDto("changed1", "changed2",
                "changed@gmail.com",
                RoleName.ROLE_ADMIN.name());

        mvc.perform(put("/api/account/changeUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("changed1"))
                .andExpect(jsonPath("$.lastName").value("changed2"))
                .andExpect(jsonPath("$.email").value("changed@gmail.com"))
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void changePassword() throws Exception {
        PasswordDto passwordDto = new PasswordDto("user", "user1234");

        mvc.perform(put("/api/account/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        //check if password was updated indeed
        UserDetails userDetails = userService.loadUserByUsername("user@gmail.com");
        assertTrue(passwordEncoder.matches("user1234", userDetails.getPassword()));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void changePasswordBadRequest() throws Exception {
        PasswordDto passwordDto = new PasswordDto("", "user1234");

        mvc.perform(put("/api/account/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
}