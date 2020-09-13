package com.gabor.hr.repository;


import com.gabor.hr.model.City;
import com.gabor.hr.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findAllByCountry(Country country);

    City findByNameAndCountry(String name, Country country);
}
