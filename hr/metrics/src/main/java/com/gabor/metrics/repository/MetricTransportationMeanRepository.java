package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricTransportationMean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricTransportationMeanRepository extends MongoRepository<MetricTransportationMean, String> {
    Optional<MetricTransportationMean> findByTransportationMean(String transportationMean);
}