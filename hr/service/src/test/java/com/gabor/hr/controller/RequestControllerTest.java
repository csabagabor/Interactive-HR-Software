package com.gabor.hr.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.gabor.hr.model.Request;
import com.gabor.hr.model.Status;
import com.gabor.hr.repository.RequestRepository;
import com.gabor.hr.service.dto.RequestDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
public class RequestControllerTest extends CrudControllerTest<RequestDto, RequestRepository> {
    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void findById() throws Exception {
        RequestDto byId = findById(1L, HttpStatus.OK);

        assertEquals("nothing", byId.getDetails());
        assertEquals("user@gmail.com", byId.getUserEmail());
        assertEquals("car", byId.getTransportationMeanName());
        assertEquals("Germany", byId.getCountryName());
        assertEquals("Munich", byId.getCityName());
        assertEquals(Status.OPEN, byId.getStatus());
        assertEquals(LocalDate.of(2019, 9, 20), byId.getStartDate());
        assertEquals(LocalDate.of(2019, 10, 1), byId.getEndDate());
        assertEquals(false, byId.isNeedOfLaptop());
        assertEquals(false, byId.isNeedOfPhone());
        assertEquals(false, byId.isNeedOfVpn());
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void findAll() throws Exception {
        List<RequestDto> list = findAll(HttpStatus.OK);

        assertEquals(9, list.size());

        assertThat(list).extracting("details")
                .contains("nothing", "blabla", "test", "test2");
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAllOwn() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/own")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, RequestDto.class);
        List<RequestDto> requests = objectMapper.readValue(content, type);

        assertThat(requests).extracting("details")
                .contains("nothing", "blabla", "test", "test2");

        assertThat(requests).extracting("details")
                .doesNotContain("test6");//other user's request
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void findAllOpen() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/open")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, RequestDto.class);
        List<RequestDto> requests = objectMapper.readValue(content, type);

        assertThat(requests).extracting("details")
                .contains("nothing", "blabla", "test", "test2");

        assertThat(requests).extracting("details")
                .doesNotContain("test5", "test6");//not open
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void findAllRejected() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/rejected")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, RequestDto.class);
        List<RequestDto> requests = objectMapper.readValue(content, type);

        assertThat(requests).extracting("details")
                .contains("test6");

        assertThat(requests).extracting("details")
                .doesNotContain("nothing", "test5");//not open
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void findAllAccepted() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/accepted")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, RequestDto.class);
        List<RequestDto> requests = objectMapper.readValue(content, type);

        assertThat(requests).extracting("details")
                .contains("test5");

        assertThat(requests).extracting("details")
                .doesNotContain("test6", "blabla");//not open
    }


    //unauthorized
    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findByIdUnauthorized() throws Exception {
        findById(1L, HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findByIdUnauthorized2() throws Exception {
        findById(1L, HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findAllUnAuthorized() throws Exception {
        findAll(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAllUnAuthorized2() throws Exception {
        findAll(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void updateUnauthorized() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        update(1L, requestDto, HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void updateUnauthorized2() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        update(1L, requestDto, HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void deleteUnauthorized() throws Exception {
        delete(1L, HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void deleteUnauthorizedNotOwnRequest() throws Exception {
        delete(9L, HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void acceptUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/" +
                getName() + "/accept/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();

        //check that it was not accepted
        assertEquals(Status.OPEN, repository.findById(1L).get().getStatus());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void rejectUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/" +
                getName() + "/reject/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();

        //check that it was not rejected
        assertEquals(Status.OPEN, repository.findById(1L).get().getStatus());
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void findAllOwnUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/own")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findAllOwnUnauthorized2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/own")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findAllRejectedUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/rejected")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void findAllAcceptedUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/accepted")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAllRejectedUnauthorized2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/rejected")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void findAllAcceptedUnauthorized2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/accepted")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    //bad request

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void saveBadRequest() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        requestDto.setCityName("");
        save(requestDto, HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void updateBadRequest() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        requestDto.setCityName("");
        update(1L, requestDto, HttpStatus.BAD_REQUEST);
    }

    //not found resources
    //bad request

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void saveNotFoundCity() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        requestDto.setCityName("not found");
        save(requestDto, HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void saveNotFoundCountry() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        requestDto.setCountryName("not found");
        save(requestDto, HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void saveNotFoundTransportationMean() throws Exception {
        RequestDto requestDto = createCorrectRequest();
        requestDto.setTransportationMeanName("not found");
        save(requestDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected String getName() {
        return "requests";
    }

    private RequestDto createCorrectRequest() {
        RequestDto requestDto = new RequestDto(LocalDate.now().plusDays(5), LocalDate.now().plusDays(10),
                true, false, false, "details",

                "car", "Romania", "Bucharest",
                Status.OPEN);
        requestDto.setUserEmail("user@gmail.com");
        return requestDto;
    }
}