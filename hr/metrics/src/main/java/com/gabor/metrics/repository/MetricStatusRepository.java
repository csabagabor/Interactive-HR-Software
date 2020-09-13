package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricStatusRepository extends MongoRepository<MetricStatus, String> {
    Optional<MetricStatus> findByStatus(String status);
}