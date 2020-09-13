package com.gabor.hr.service.dto;

import com.gabor.hr.model.Request;
import com.gabor.hr.model.Status;
import com.gabor.hr.repository.CityRepository;
import com.gabor.hr.repository.CountryRepository;
import com.gabor.hr.repository.TransportationMeanRepository;
import com.gabor.hr.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ModelMapper.class})
public class RequestModelMapprTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransportationMeanRepository transportationMeanRepository;

    @Test
    public void mappingCorrectDtoToModel() {
        var dto = new RequestDto(LocalDate.of(2018, 2, 2),
                LocalDate.of(2019, 2, 2),
                false, false, true,
                "details",
                "car", "Romania", "Bucharest",
                Status.OPEN);
        dto.setUserEmail("user@gmail.com");

        var model = modelMapper.map(dto, Request.class);

        assertEquals(dto.getStatus(), model.getStatus());
        assertEquals(dto.getStartDate(), model.getStartDate());
        assertEquals(dto.getEndDate(), model.getEndDate());
        assertEquals(dto.getUserEmail(), model.getUser().getEmail());
        assertEquals(dto.getTransportationMeanName(), model.getTransportationMean().getName());
        assertEquals(dto.getCityName(), model.getCity().getName());
    }

    @Test
    public void mappingCorrectModelToDto() {

        var country = countryRepository.findByName("Romania");
        var city = cityRepository.findByNameAndCountry("Bucharest", countryRepository.findByName("Romania"));
        var user = userRepository.findByEmail("user@gmail.com");
        var transportationMean = transportationMeanRepository.findByName("car");

        var model = new Request(LocalDate.of(2018, 2, 2),
                LocalDate.of(2019, 2, 2),
                false, false, true,
                "details", Status.OPEN, user, transportationMean,
                 city);


        var dto = modelMapper.map(model, RequestDto.class);

        assertEquals(model.getCity().getName(), dto.getCityName());
        assertEquals(model.getEndDate(), dto.getEndDate());
        assertEquals(model.getStartDate(), dto.getStartDate());
        assertEquals(model.getStatus(), dto.getStatus());
        assertEquals(model.getTransportationMean().getName(), dto.getTransportationMeanName());
        assertEquals(model.getUser().getEmail(), dto.getUserEmail());
    }
}