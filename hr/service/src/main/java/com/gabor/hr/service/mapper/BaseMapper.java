package com.gabor.hr.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

class BaseMapper {
    @Autowired
    protected ModelMapper modelMapper;
}
