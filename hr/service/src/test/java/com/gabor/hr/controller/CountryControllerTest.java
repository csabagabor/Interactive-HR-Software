package com.gabor.hr.controller;

import com.gabor.hr.model.Country;
import com.gabor.hr.repository.CountryRepository;
import com.gabor.hr.service.dto.CountryDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CountryControllerTest extends CrudControllerTest<CountryDto, CountryRepository> {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findById() throws Exception {
        CountryDto byId = findById(1L, HttpStatus.OK);

        assertEquals("Germany", byId.getName());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAll() throws Exception {
        List<CountryDto> list = findAll(HttpStatus.OK);

        assertEquals(3, list.size());
        Assertions.assertThat(list).extracting("name")
                .contains("Romania", "Germany", "USA");
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void save() throws Exception {
        CountryDto byId = save(new CountryDto("Country"), HttpStatus.OK);

        assertEquals("Country", byId.getName());

        //check that it was saved indeed in the DB
        Country saved = repository.findById(byId.getId()).get();
        assertEquals("Country", saved.getName());
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void update() throws Exception {
        CountryDto byId = update(1L, new CountryDto("Country"), HttpStatus.OK);

        assertEquals("Country", byId.getName());

        //check that it was updated indeed in the DB
        Country saved = repository.findById(byId.getId()).get();
        assertEquals("Country", saved.getName());
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void delete() throws Exception {
        delete(1L, HttpStatus.OK);

        //check that it was deleted indeed from the DB
        assertEquals(Optional.empty(), repository.findById(1L));
    }

    //unauthorized for user
    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void saveUnauthorized() throws Exception {
        save(new CountryDto("Country"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateUnauthorized() throws Exception {
        update(1L, new CountryDto("Country"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void deleteUnauthorized() throws Exception {
        delete(1L, HttpStatus.FORBIDDEN);
    }

    //bad request

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveBadRequest() throws Exception {
        save(new CountryDto(""), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateBadRequest() throws Exception {
        update(1L, new CountryDto(""), HttpStatus.BAD_REQUEST);
    }

    //duplicate

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveDuplicate() throws Exception {
        save(new CountryDto("Romania"), HttpStatus.BAD_REQUEST);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateDuplicate() throws Exception {
        update(1L, new CountryDto("Romania"), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected String getName() {
        return "countries";
    }
}