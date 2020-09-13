package com.gabor.hr.service;

import com.gabor.hr.model.TransportationMean;
import com.gabor.hr.repository.TransportationMeanRepository;
import com.gabor.hr.service.dto.TransportationMeanDto;
import org.springframework.stereotype.Service;

@Service
public class TransportationMeanService extends CrudService<TransportationMean, TransportationMeanDto, TransportationMeanRepository> {

}
