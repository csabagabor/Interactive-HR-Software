package com.gabor.hr.service.dto;

import com.gabor.hr.model.User;
import com.gabor.hr.repository.RoleRepository;
import com.gabor.hr.service.mapper.UserMapper;
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
@Import({ModelMapper.class, UserMapper.class})
public class UserDtoMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void mappingCorrectDtoToModel() {
        UserDto dto = new UserDto("firstName", "lastName", "email", "ROLE_ADMIN");

        User model = userMapper.userDtoToUser(dto, "hash");

        assertEquals(dto.getEmail(), model.getEmail());
        assertEquals(dto.getRoleName(), model.getRole().getName());
        assertEquals(dto.getFirstName(), model.getFirstName());
        assertEquals(dto.getLastName(), model.getLastName());
        assertEquals("hash", model.getPasswordHash());
    }

    @Test
    public void mappingCorrectModelToDto() {

//        User model = new User("firstName", "lastName", "email", "hash",
//                roleRepository.findByName("ROLE_ADMIN"),
//                null);
//
//        UserDto dto = modelMapper.map(model, UserDto.class);
//
//        assertEquals(model.getEmail(), dto.getEmail());
//        assertEquals(model.getRole().getName(), dto.getRoleName());
//        assertEquals(model.getFirstName(), dto.getFirstName());
//        assertEquals(model.getLastName(), dto.getLastName());
    }
}