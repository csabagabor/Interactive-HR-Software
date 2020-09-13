package com.gabor.hr.service.dto;

import com.gabor.hr.model.Role;
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
public class RoleDtoMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void mappingCorrectDtoToModel() {
        RoleDto dto = new RoleDto("Role");
        Role model = modelMapper.map(dto, Role.class);

        assertEquals(dto.getName(), model.getName());
    }

    @Test
    public void mappingCorrectModelToDto() {

        Role model = new Role("Role");
        RoleDto dto = modelMapper.map(model, RoleDto.class);

        assertEquals(model.getName(), dto.getName());
    }
}