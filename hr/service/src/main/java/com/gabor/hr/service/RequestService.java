package com.gabor.hr.service;

import com.gabor.hr.exception.ResourceNotFoundException;
import com.gabor.hr.model.Request;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.User;
import com.gabor.hr.repository.RequestRepository;
import com.gabor.hr.repository.UserRepository;
import com.gabor.hr.service.dto.RequestDto;
import com.gabor.model.dto.RequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class RequestService extends CrudService<Request, RequestDto, RequestRepository> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public RequestDto save(RequestDto obj) {
        obj.setStatus(Status.OPEN);

        RequestDto res = super.save(obj);

        //send async message with rabbitMQ to calculate metrics
        messageService.sendSaveRequest(modelMapper.map(res, RequestModel.class));

        return res;
    }

    @Override
    public RequestDto update(RequestDto obj) {
        //checks if it exists(else throws exception)
        RequestDto old = findById(obj.getId());

        Request savedObj = repository.save(modelMapper.map(obj, Request.class));

        RequestDto res = modelMapper.map(savedObj, RequestDto.class);

        messageService.sendUpdate(modelMapper.map(old, RequestModel.class), modelMapper.map(res, RequestModel.class));

        return res;
    }

    @Override
    public void delete(Long id) {
        //checks if it exists(else throws exception)
        RequestDto byId = findById(id);

        repository.deleteById(id);

        messageService.sendDelete(modelMapper.map(byId, RequestModel.class));
    }

    public List<RequestDto> findAllByUser(String userEmail) {

        User byEmail = userRepository.findByEmail(userEmail);

        if (byEmail == null) {
            throw new ResourceNotFoundException("User", "email", userEmail);
        }

        return repository.findAllByUser(byEmail).stream()
                .map(obj -> modelMapper.map(obj, RequestDto.class))
                .collect(Collectors.toList());
    }

    public List<RequestDto> findAllByStatus(Status status) {
        return repository.findAllByStatus(status).stream()
                .map(obj -> modelMapper.map(obj, RequestDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void accept(Long id) {
        //send async message with rabbitMQ to calculate metrics
        RequestDto byId = findById(id);
        messageService.sendAcceptRequest(modelMapper.map(byId, RequestModel.class));

        repository.accept(id);
    }

    @Transactional
    public void reject(Long id) {
        RequestDto byId = findById(id);
        messageService.sendRejectRequest(modelMapper.map(byId, RequestModel.class));

        repository.reject(id);
    }

    public Map<Long, Map<Long, Long>> getMetricsWithRequestsPerMonth() {
        List<RequestDto> requests = findAll();

        Map<Long, Map<Long, Long>> metrics = new HashMap<>();

        for (RequestDto request : requests) {
            LocalDate startDate = request.getStartDate();
            long year = startDate.getYear();
            long month = startDate.getMonth().getValue();

            Map<Long, Long> yearMap = metrics.get(year);
            if (yearMap == null) {
                metrics.put(year, new HashMap<>());
                yearMap = metrics.get(year);
            }

            Long monthCounter = yearMap.get(month);
            if (monthCounter == null) {
                yearMap.put(month, 1L);
            } else {
                //increment counter
                yearMap.put(month, yearMap.get(month) + 1);
            }
        }

        return metrics;
    }

    public Map<String, Long> getMetricsWithRequestsPerCountry() {
        return calculatedMetricsBasedOn(RequestDto::getCountryName);
    }

    public Map<String, Long> getMetricsWithRequestsPerTransportationMean() {
        return calculatedMetricsBasedOn(RequestDto::getTransportationMeanName);
    }

    public Map<Status, Long> getMetricsWithRequestsPerStatus() {
        return calculatedMetricsBasedOn(RequestDto::getStatus);
    }

    public Map<Boolean, Long> getMetricsWithRequestsPerLaptop() {
        return calculatedMetricsBasedOn(RequestDto::isNeedOfLaptop);
    }

    public Map<String, Long> getMetricsWithRequestsPerCity() {
        return calculatedMetricsBasedOn(RequestDto::getCityName);
    }

    public Map<Long, Long> getMetricsWithRequestsPerDuration() {
        return calculatedMetricsBasedOn(request -> {
                    long daysDifference = DAYS.between(request.getStartDate(), request.getEndDate());
                    if (daysDifference > 31) {
                        daysDifference = 31;
                    }

                    return daysDifference;
                }
        );
    }

    private <T> Map<T, Long> calculatedMetricsBasedOn(Function<RequestDto, T> func) {
        List<RequestDto> requests = findAll();

        Map<T, Long> metrics = new HashMap<>();

        for (RequestDto request : requests) {
            T param = func.apply(request);

            Long counter = metrics.get(param);
            if (counter == null) {
                metrics.put(param, 1L);
            } else {//increment counter
                metrics.put(param, metrics.get(param) + 1);
            }
        }

        return metrics;
    }
}
