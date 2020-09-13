package com.gabor.hr.controller;

import com.gabor.model.model.RoleName;
import com.gabor.hr.model.User;
import com.gabor.hr.repository.UserRepository;
import com.gabor.hr.service.EmailService;
import com.gabor.hr.service.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@ActiveProfiles("test")
@Transactional
public class UserControllerTest extends CrudControllerTest<UserDto, UserRepository> {
    //do not send real email
    @MockBean
    private EmailService emailService;

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findById() throws Exception {
        UserDto byId = findById(1L, HttpStatus.OK);

        assertEquals("admin@gmail.com", byId.getEmail());
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findAll() throws Exception {
        List<UserDto> list = findAll(HttpStatus.OK);

        assertEquals(6, list.size());
        Assertions.assertThat(list).extracting("email")
                .contains("user@gmail.com", "user2@gmail.com", "admin@gmail.com",
                        "moderator@gmail.com",
                        "pm@gmail.com",
                        "payroll@gmail.com");
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void save() throws Exception {
        UserDto byId = save(new UserDto("FirstName", "LastName",
                        "email@gmail.com", RoleName.ROLE_USER.name()),
                HttpStatus.OK);

        assertEquals("email@gmail.com", byId.getEmail());
        assertEquals(RoleName.ROLE_USER.name(), byId.getRoleName());
        assertEquals("FirstName", byId.getFirstName());
        assertEquals("LastName", byId.getLastName());

        //check that it was saved indeed in the DB
        User saved = repository.findById(byId.getId()).get();
        assertEquals("email@gmail.com", saved.getEmail());
        assertEquals(RoleName.ROLE_USER.name(), saved.getRole().getName());
        assertEquals("FirstName", saved.getFirstName());
        assertEquals("LastName", saved.getLastName());

        //verify that email was sent
        verify(emailService).sendSimpleMessage(eq("email@gmail.com"), anyString(), anyString());
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void update() throws Exception {
        UserDto byId = update(1L, new UserDto("FirstName", "LastName",
                "email@gmail.com", RoleName.ROLE_USER.name()), HttpStatus.OK);

        assertEquals("email@gmail.com", byId.getEmail());
        assertEquals(RoleName.ROLE_USER.name(), byId.getRoleName());
        assertEquals("FirstName", byId.getFirstName());
        assertEquals("LastName", byId.getLastName());

        //check that it was updated indeed in the DB
        User saved = repository.findById(byId.getId()).get();
        assertEquals("email@gmail.com", saved.getEmail());
        assertEquals(RoleName.ROLE_USER.name(), saved.getRole().getName());
        assertEquals("FirstName", saved.getFirstName());
        assertEquals("LastName", saved.getLastName());
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void delete() throws Exception {
        delete(1L, HttpStatus.OK);

        //check that it was deleted indeed from the DB
        assertEquals(Optional.empty(), repository.findById(1L));
    }

    //unauthorized for moderator
    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveUnauthorized() throws Exception {
        save(new UserDto("FirstName", "LastName",
                "email@gmail.com", RoleName.ROLE_USER.name()), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateUnauthorized() throws Exception {
        update(1L, new UserDto("FirstName", "LastName",
                "email@gmail.com", RoleName.ROLE_USER.name()), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void deleteUnauthorized() throws Exception {
        delete(1L, HttpStatus.FORBIDDEN);
    }

    //bad request
    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void saveBadRequest() throws Exception {
        save(new UserDto("", "LastName",
                        "email@gmail.com", RoleName.ROLE_USER.name()),
                HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void updateBadRequest() throws Exception {
        update(1L, new UserDto("", "LastName",
                "email@gmail.com", RoleName.ROLE_USER.name()), HttpStatus.BAD_REQUEST);
    }

    //duplicate
    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void saveDuplicate() throws Exception {
        save(new UserDto("aaa", "LastName",
                        "user@gmail.com", RoleName.ROLE_USER.name()),
                HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        update(1L, new UserDto("aaa", "LastName",
                "user@gmail.com", RoleName.ROLE_USER.name()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected String getName() {
        return "users";
    }


}