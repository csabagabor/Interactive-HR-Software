package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricCity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricCityRepository extends MongoRepository<MetricCity, String> {
    Optional<MetricCity> findByCity(String city);
}