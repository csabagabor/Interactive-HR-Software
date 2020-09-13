package com.gabor.hr.service.dto;

import com.gabor.hr.model.City;
import com.gabor.hr.repository.CountryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ModelMapper.class})
public class CityDtoMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void mappingCorrectDtoToModel() {
        CityDto dto = new CityDto("city", "Germany");
        City model = modelMapper.map(dto, City.class);

        assertEquals(dto.getCountryName(), model.getCountry().getName());
        assertEquals(dto.getName(), model.getName());
    }

    @Test
    public void mappingCorrectModelToDto() {

        City model = new City("city", countryRepository.findByName("Germany"));
        CityDto dto = modelMapper.map(model, CityDto.class);

        assertEquals(model.getCountry().getName(), dto.getCountryName());
        assertEquals(model.getName(), dto.getName());
    }
}