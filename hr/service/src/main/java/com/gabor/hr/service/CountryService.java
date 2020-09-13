package com.gabor.hr.service;

import com.gabor.hr.model.Country;
import com.gabor.hr.repository.CountryRepository;
import com.gabor.hr.service.dto.CountryDto;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends CrudService<Country, CountryDto, CountryRepository> {

}
