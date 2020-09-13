package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricYear;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricYearRepository extends MongoRepository<MetricYear, String> {
    Optional<MetricYear> findByYear(String year);
}