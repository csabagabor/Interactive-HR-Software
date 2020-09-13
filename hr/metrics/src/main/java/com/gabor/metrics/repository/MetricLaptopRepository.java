package com.gabor.metrics.repository;

import com.gabor.metrics.model.MetricLaptop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricLaptopRepository extends MongoRepository<MetricLaptop, String> {
    Optional<MetricLaptop> findByLaptop(String laptop);
}