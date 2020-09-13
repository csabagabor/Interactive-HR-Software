package com.gabor.hr.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
public class MetricsControllerTest extends BaseControllerTest {

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerMonth() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-month")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<Long, Map<Long, Long>>> typeRef
                = new TypeReference<>() {
        };

        Map<Long, Map<Long, Long>> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(1L, (long) metrics.get(2017L).get(1L));
        assertEquals(1L, (long) metrics.get(2017L).get(3L));
        assertEquals(1L, (long) metrics.get(2017L).get(4L));
        assertEquals(1L, (long) metrics.get(2017L).get(5L));
        assertEquals(1L, (long) metrics.get(2018L).get(9L));
        assertEquals(1L, (long) metrics.get(2019L).get(3L));
        assertEquals(1L, (long) metrics.get(2019L).get(7L));
        assertEquals(2L, (long) metrics.get(2019L).get(9L));
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerCountry() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-country")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<String, Long>> typeRef
                = new TypeReference<>() {
        };

        Map<String, Long> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(2L, (long) metrics.get("Germany"));
        assertEquals(4L, (long) metrics.get("USA"));
        assertEquals(3L, (long) metrics.get("Romania"));
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerTransportationMean() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-transportation-mean")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<String, Long>> typeRef
                = new TypeReference<>() {
        };

        Map<String, Long> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(3L, (long) metrics.get("car"));
        assertEquals(2L, (long) metrics.get("airplane"));
        assertEquals(1L, (long) metrics.get("train"));
        assertEquals(3L, (long) metrics.get("bus"));
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerStatus() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-status")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<String, Long>> typeRef
                = new TypeReference<>() {
        };

        Map<String, Long> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(7L, (long) metrics.get("OPEN"));
        assertEquals(1L, (long) metrics.get("ACCEPTED"));
        assertEquals(1L, (long) metrics.get("REJECTED"));
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerLaptop() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-laptop")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<String, Long>> typeRef
                = new TypeReference<>() {
        };

        Map<String, Long> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(4L, (long) metrics.get("false"));
        assertEquals(5L, (long) metrics.get("true"));
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerCity() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-city")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<String, Long>> typeRef
                = new TypeReference<>() {
        };

        Map<String, Long> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(2L, (long) metrics.get("Munich"));
        assertEquals(1L, (long) metrics.get("New York"));
        assertEquals(3L, (long) metrics.get("Chicago"));
        assertEquals(3L, (long) metrics.get("Bucharest"));
    }

    @Test
    @WithUserDetails(value = "moderator@gmail.com")
    public void getMetricsWithRequestsPerDuration() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-duration")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TypeReference<Map<String, Long>> typeRef
                = new TypeReference<>() {
        };

        Map<String, Long> metrics = objectMapper.readValue(content, typeRef);
        assertEquals(2L, (long) metrics.get("5"));
        assertEquals(5L, (long) metrics.get("6"));
        assertEquals(2L, (long) metrics.get("11"));
    }

    //Unauthorized

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerMonthUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-month")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerCountryUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-country")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerTransportationMeanUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-transportation-mean")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerStatusUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-status")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerLaptopUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-laptop")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerCityUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-city")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void getMetricsWithRequestsPerDurationUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/metrics/requests-per-duration")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }
}