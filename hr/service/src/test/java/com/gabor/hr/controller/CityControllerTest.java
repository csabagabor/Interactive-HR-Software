package com.gabor.hr.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.gabor.hr.model.City;
import com.gabor.hr.repository.CityRepository;
import com.gabor.hr.service.dto.CityDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Transactional
public class CityControllerTest extends CrudControllerTest<CityDto, CityRepository> {
    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAllByCountry() throws Exception {
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get("/api/" + getName() + "/country/USA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, CityDto.class);
        List<CityDto> list = objectMapper.readValue(content, type);

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(t -> t.getName().equals("New York")));
        assertTrue(list.stream().anyMatch(t -> t.getName().equals("Chicago")));
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findById() throws Exception {
        CityDto byId = findById(1L, HttpStatus.OK);

        assertEquals("Germany", byId.getCountryName());
        assertEquals("Munich", byId.getName());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAll() throws Exception {
        List<CityDto> list = findAll(HttpStatus.OK);

        assertEquals(4, list.size());
        Assertions.assertThat(list).extracting("name")
                .contains("New York", "Chicago", "Bucharest", "Munich");
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void save() throws Exception {
        CityDto byId = save(new CityDto("name", "Romania"), HttpStatus.OK);

        assertEquals("Romania", byId.getCountryName());
        assertEquals("name", byId.getName());

        //check that it was saved indeed in the DB
        City saved = repository.findById(byId.getId()).get();
        assertEquals("Romania", saved.getCountry().getName());
        assertEquals("name", saved.getName());
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void update() throws Exception {
        CityDto byId = update(1L, new CityDto("name", "Germany"), HttpStatus.OK);

        assertEquals("Germany", byId.getCountryName());
        assertEquals("name", byId.getName());

        //check that it was updated indeed in the DB
        City saved = repository.findById(byId.getId()).get();
        assertEquals("Germany", saved.getCountry().getName());
        assertEquals("name", saved.getName());
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
        save(new CityDto("name", "Romania"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateUnauthorized() throws Exception {
        update(1L, new CityDto("name", "Germany"), HttpStatus.FORBIDDEN);
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
        save(new CityDto("", "Romania"), HttpStatus.BAD_REQUEST);
    }


    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateBadRequest() throws Exception {
        update(1L, new CityDto("", "Germany"), HttpStatus.BAD_REQUEST);
    }

    //duplicate
    //bad request
    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveDuplicate() throws Exception {
        save(new CityDto("Bucharest", "Romania"), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveOnlyCityNotDuplicate() throws Exception {
        save(new CityDto("Bucharest", "Germany"), HttpStatus.OK);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        update(1L, new CityDto("Bucharest", "Romania"), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateOnlyCityNotDuplicate() throws Exception {
        update(1L, new CityDto("Bucharest", "Germany"), HttpStatus.OK);
    }

    @Override
    protected String getName() {
        return "cities";
    }
}