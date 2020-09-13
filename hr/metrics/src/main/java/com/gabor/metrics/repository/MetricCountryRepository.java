package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricCity;
import com.gabor.metrics.model.MetricCountry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricCountryRepository extends MongoRepository<MetricCountry, String> {
    Optional<MetricCountry> findByCountry(String country);
}