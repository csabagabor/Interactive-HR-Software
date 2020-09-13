package com.gabor.hr.service.mapper;

import com.gabor.hr.model.Role;
import com.gabor.hr.model.User;
import com.gabor.hr.repository.RoleRepository;
import com.gabor.hr.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper {

    @Autowired
    private RoleRepository roleRepository;

    public User userDtoToUser(UserDto userDto, String passwordHash) {
        User user = modelMapper.map(userDto, User.class);

        Role role = roleRepository.findByName(userDto.getRoleName());
        user.setRole(role);
        user.setPasswordHash(passwordHash);

        return user;
    }

}
