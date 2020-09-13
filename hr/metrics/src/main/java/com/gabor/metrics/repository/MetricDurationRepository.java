package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricDuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricDurationRepository extends MongoRepository<MetricDuration, String> {
    Optional<MetricDuration> findByDuration(String duration);
}