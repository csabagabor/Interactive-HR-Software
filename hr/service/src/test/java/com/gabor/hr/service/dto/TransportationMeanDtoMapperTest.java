package com.gabor.hr.service.dto;

import com.gabor.hr.model.TransportationMean;
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
public class TransportationMeanDtoMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void mappingCorrectDtoToModel() {
        TransportationMeanDto dto = new TransportationMeanDto("TransportationMean");
        TransportationMean model = modelMapper.map(dto, TransportationMean.class);

        assertEquals(dto.getName(), model.getName());
    }

    @Test
    public void mappingCorrectModelToDto() {

        TransportationMean model = new TransportationMean("TransportationMean");
        TransportationMeanDto dto = modelMapper.map(model, TransportationMeanDto.class);

        assertEquals(model.getName(), dto.getName());
    }
}