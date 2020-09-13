package com.gabor.hr.repository;


import com.gabor.hr.model.TransportationMean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationMeanRepository extends JpaRepository<TransportationMean, Long> {
    TransportationMean findByName(String name);
}
