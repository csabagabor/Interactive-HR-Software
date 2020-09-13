package com.gabor.hr.service;

import com.gabor.hr.model.City;
import com.gabor.hr.model.Country;
import com.gabor.hr.repository.CityRepository;
import com.gabor.hr.repository.CountryRepository;
import com.gabor.hr.service.dto.CityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService extends CrudService<City, CityDto, CityRepository> {
    @Autowired
    private CountryRepository countryRepository;

    public List<City> findAllByCountry(String countryName) {
        Country country = countryRepository.findByName(countryName);
        return repository.findAllByCountry(country);
    }
}
