package com.gabor.hr.service;

import com.gabor.hr.model.Role;
import com.gabor.hr.repository.RoleRepository;
import com.gabor.hr.service.dto.RoleDto;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends CrudService<Role, RoleDto, RoleRepository> {

}
