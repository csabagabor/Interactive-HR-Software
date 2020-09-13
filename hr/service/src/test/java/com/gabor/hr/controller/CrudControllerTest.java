package com.gabor.hr.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.gabor.hr.service.dto.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class CrudControllerTest<D extends BaseDto,
        Repo extends JpaRepository<?, Long>> extends BaseControllerTest {

    @Autowired
    protected Repo repository;

    private final Class<D> dtoType;

    public CrudControllerTest() {
        dtoType = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    protected D findById(Long id, HttpStatus status) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/" + getName() + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(status.value()))
                .andReturn();

        if (status != HttpStatus.OK) {
            return null;
        }

        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, dtoType);
    }

    protected List<D> findAll(HttpStatus status) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/" + getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(status.value()))
                .andReturn();

        if (status != HttpStatus.OK) {
            return null;
        }

        String content = mvcResult.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, dtoType);
        return objectMapper.readValue(content, type);
    }

    protected D save(D object, HttpStatus status) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/" + getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(status.value()))
                .andReturn();

        if (status != HttpStatus.OK) {
            return null;
        }

        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, dtoType);
    }

    protected D update(Long id, D object, HttpStatus status) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/" + getName() + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(status.value()))
                .andReturn();

        if (status != HttpStatus.OK) {
            return null;
        }

        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, dtoType);
    }

    protected void delete(Long id, HttpStatus status) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/" + getName() + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(status.value()));

        if (status != HttpStatus.OK) {
            return;
        }

    }

    protected abstract String getName();
}
