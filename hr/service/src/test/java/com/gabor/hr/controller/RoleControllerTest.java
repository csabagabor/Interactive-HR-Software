package com.gabor.hr.controller;

import com.gabor.hr.model.Role;
import com.gabor.hr.repository.RoleRepository;
import com.gabor.hr.service.dto.RoleDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@Transactional
public class RoleControllerTest extends CrudControllerTest<RoleDto, RoleRepository> {
    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findById() throws Exception {
        RoleDto byId = findById(1L, HttpStatus.OK);

        assertEquals("ROLE_ADMIN", byId.getName());
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findAll() throws Exception {
        List<RoleDto> list = findAll(HttpStatus.OK);

        assertEquals(5, list.size());
        Assertions.assertThat(list).extracting("name")
                .contains("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_PM", "ROLE_PAYROLL");
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void save() throws Exception {
        RoleDto byId = save(new RoleDto("Role"), HttpStatus.OK);

        assertEquals("Role", byId.getName());

        //check that it was saved indeed in the DB
        Role saved = repository.findById(byId.getId()).get();
        assertEquals("Role", saved.getName());
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void update() throws Exception {
        RoleDto byId = update(1L, new RoleDto("Role"), HttpStatus.OK);

        assertEquals("Role", byId.getName());

        //check that it was updated indeed in the DB
        Role saved = repository.findById(byId.getId()).get();
        assertEquals("Role", saved.getName());
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
    public void saveUser() throws Exception {
        save(new RoleDto("Role"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateUser() throws Exception {
        update(1L, new RoleDto("Role"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void deleteUser() throws Exception {
        delete(1L, HttpStatus.FORBIDDEN);
    }

    //bad request

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void saveBadRequest() throws Exception {
        save(new RoleDto(""), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void updateBadRequest() throws Exception {
        update(1L, new RoleDto(""), HttpStatus.BAD_REQUEST);
    }

    //bad request

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void saveDuplicate() throws Exception {
        save(new RoleDto("ROLE_USER"), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        update(1L, new RoleDto("ROLE_USER"), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected String getName() {
        return "roles";
    }

}