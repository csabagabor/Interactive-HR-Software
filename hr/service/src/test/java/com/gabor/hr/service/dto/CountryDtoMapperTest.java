package com.gabor.hr.service.dto;

import com.gabor.hr.model.Country;
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
public class CountryDtoMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void mappingCorrectDtoToModel() {
        CountryDto dto = new CountryDto("Country");
        Country model = modelMapper.map(dto, Country.class);

        assertEquals(dto.getName(), model.getName());
    }

    @Test
    public void mappingCorrectModelToDto() {

        Country model = new Country("Country");
        CountryDto dto = modelMapper.map(model, CountryDto.class);

        assertEquals(model.getName(), dto.getName());
    }
}