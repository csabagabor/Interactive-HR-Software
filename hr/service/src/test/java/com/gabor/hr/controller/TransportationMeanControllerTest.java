package com.gabor.hr.controller;

import com.gabor.hr.model.TransportationMean;
import com.gabor.hr.repository.TransportationMeanRepository;
import com.gabor.hr.service.dto.TransportationMeanDto;
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
public class TransportationMeanControllerTest extends
        CrudControllerTest<TransportationMeanDto, TransportationMeanRepository> {
    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findById() throws Exception {
        TransportationMeanDto byId = findById(1L, HttpStatus.OK);

        assertEquals("car", byId.getName());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAll() throws Exception {
        List<TransportationMeanDto> list = findAll(HttpStatus.OK);

        assertEquals(4, list.size());
        Assertions.assertThat(list).extracting("name")
                .contains("airplane", "car", "train", "bus");
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void save() throws Exception {
        TransportationMeanDto byId = save(new TransportationMeanDto("TransportationMean"), HttpStatus.OK);

        assertEquals("TransportationMean", byId.getName());

        //check that it was saved indeed in the DB
        TransportationMean saved = repository.findById(byId.getId()).get();
        assertEquals("TransportationMean", saved.getName());
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void update() throws Exception {
        TransportationMeanDto byId = update(1L, new TransportationMeanDto("TransportationMean"), HttpStatus.OK);

        assertEquals("TransportationMean", byId.getName());

        //check that it was updated indeed in the DB
        TransportationMean saved = repository.findById(byId.getId()).get();
        assertEquals("TransportationMean", saved.getName());
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
    public void saveUser() throws Exception {
        save(new TransportationMeanDto("TransportationMean"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateUser() throws Exception {
        update(1L, new TransportationMeanDto("TransportationMean"), HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void deleteUser() throws Exception {
        delete(1L, HttpStatus.FORBIDDEN);
    }

    //bad request

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveBadRequest() throws Exception {
        save(new TransportationMeanDto(""), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateBadRequest() throws Exception {
        update(1L, new TransportationMeanDto(""), HttpStatus.BAD_REQUEST);
    }

    //duplicate

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void saveDuplicate() throws Exception {
        save(new TransportationMeanDto("car"), HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        TransportationMean saved = repository.findById(2L).get();
        assertEquals("airplane", saved.getName());

        update(2L, new TransportationMeanDto("car"), HttpStatus.BAD_REQUEST);

        //check that it wasn't updated indeed in the DB
        saved = repository.findById(2L).get();
        assertEquals("airplane", saved.getName());
    }

    //other invalid
    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void badIdInBody() throws Exception {
        TransportationMeanDto transportationMean = new TransportationMeanDto("TransportationMean");
        transportationMean.setId(2L);
        update(1L, transportationMean, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected String getName() {
        return "transportation-means";
    }

}