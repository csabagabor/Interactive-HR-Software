package com.gabor.metrics.service;

import com.gabor.metrics.model.BaseMetric;
import com.gabor.metrics.model.MetricCity;
import com.gabor.metrics.model.MetricCountry;
import com.gabor.metrics.model.MetricDuration;
import com.gabor.metrics.model.MetricLaptop;
import com.gabor.metrics.model.MetricStatus;
import com.gabor.metrics.model.MetricTransportationMean;
import com.gabor.metrics.model.MetricYear;
import com.gabor.metrics.repository.MetricCityRepository;
import com.gabor.metrics.repository.MetricCountryRepository;
import com.gabor.metrics.repository.MetricDurationRepository;
import com.gabor.metrics.repository.MetricLaptopRepository;
import com.gabor.metrics.repository.MetricStatusRepository;
import com.gabor.metrics.repository.MetricTransportationMeanRepository;
import com.gabor.metrics.repository.MetricYearRepository;
import com.gabor.model.dto.RequestModel;
import com.gabor.model.dto.RequestUpdateModel;
import com.gabor.model.dto.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class AmqpService {
    @Autowired
    private MetricCityRepository metricCityRepository;

    @Autowired
    private MetricTransportationMeanRepository transportationMeanRepository;

    @Autowired
    private MetricCountryRepository metricCountryRepository;

    @Autowired
    private MetricDurationRepository metricDurationRepository;

    @Autowired
    private MetricStatusRepository metricStatusRepository;

    @Autowired
    private MetricLaptopRepository metricLaptopRepository;

    @Autowired
    private MetricYearRepository metricYearRepository;

    @RabbitListener(queues = {"${queue.request.save.name}"})
    public void receiveMessageSaveRequest(RequestModel requestModel) {
        log.info("save: " + requestModel);

        //calc year based metric
        incrementYearMetric(requestModel);

        incrementMetric(MetricCity.class, metricCityRepository, metricCityRepository::findByCity,
                MetricCity::setCity, requestModel.getCityName());

        incrementMetric(MetricCountry.class, metricCountryRepository, metricCountryRepository::findByCountry,
                MetricCountry::setCountry, requestModel.getCountryName());

        incrementMetric(MetricLaptop.class, metricLaptopRepository, metricLaptopRepository::findByLaptop,
                MetricLaptop::setLaptop, String.valueOf(requestModel.isNeedOfLaptop()));

        incrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                MetricStatus::setStatus, requestModel.getStatus().name());

        long daysDifference = DAYS.between(requestModel.getStartDate(), requestModel.getEndDate());
        if (daysDifference > 31) {
            daysDifference = 31;
        }

        incrementMetric(MetricDuration.class, metricDurationRepository, metricDurationRepository::findByDuration,
                MetricDuration::setDuration, String.valueOf(daysDifference));

        incrementMetric(MetricTransportationMean.class, transportationMeanRepository, transportationMeanRepository::findByTransportationMean,
                MetricTransportationMean::setTransportationMean, String.valueOf(requestModel.getTransportationMeanName()));
    }

    @RabbitListener(queues = {"${queue.request.update.name}"})
    public void receiveMessageUpdateRequest(RequestUpdateModel requestUpdateModel) {
        log.info("update: " + requestUpdateModel);

        RequestModel oldModel = requestUpdateModel.getOldModel();
        RequestModel newModel = requestUpdateModel.getNewModel();

        //calc year based metric
        incrementYearMetric(newModel);
        decrementYearMetric(oldModel);

        //check what has changed
        if (!oldModel.getCityName().equals(newModel.getCityName())) {
            decrementMetric(MetricCity.class, metricCityRepository, metricCityRepository::findByCity,
                    oldModel.getCityName());

            incrementMetric(MetricCity.class, metricCityRepository, metricCityRepository::findByCity,
                    MetricCity::setCity, newModel.getCityName());
        }

        if (!oldModel.getCountryName().equals(newModel.getCountryName())) {
            decrementMetric(MetricCountry.class, metricCountryRepository, metricCountryRepository::findByCountry,
                    oldModel.getCountryName());

            incrementMetric(MetricCountry.class, metricCountryRepository, metricCountryRepository::findByCountry,
                    MetricCountry::setCountry, newModel.getCountryName());
        }

        if (!oldModel.getStatus().equals(newModel.getStatus())) {
            decrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                    oldModel.getStatus().name());

            incrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                    MetricStatus::setStatus, newModel.getStatus().name());
        }

        if (!oldModel.getTransportationMeanName().equals(newModel.getTransportationMeanName())) {
            decrementMetric(MetricTransportationMean.class, transportationMeanRepository, transportationMeanRepository::findByTransportationMean,
                    String.valueOf(oldModel.getTransportationMeanName()));

            incrementMetric(MetricTransportationMean.class, transportationMeanRepository, transportationMeanRepository::findByTransportationMean,
                    MetricTransportationMean::setTransportationMean, String.valueOf(newModel.getTransportationMeanName()));
        }

        long oldDaysDifference = DAYS.between(oldModel.getStartDate(), oldModel.getEndDate());
        if (oldDaysDifference > 31) {
            oldDaysDifference = 31;
        }

        long newDaysDifference = DAYS.between(newModel.getStartDate(), newModel.getEndDate());
        if (newDaysDifference > 31) {
            newDaysDifference = 31;
        }


        if (oldDaysDifference != newDaysDifference) {
            decrementMetric(MetricDuration.class, metricDurationRepository, metricDurationRepository::findByDuration,
                    String.valueOf(oldDaysDifference));

            incrementMetric(MetricDuration.class, metricDurationRepository, metricDurationRepository::findByDuration,
                    MetricDuration::setDuration, String.valueOf(newDaysDifference));
        }

        if (oldModel.isNeedOfLaptop() != newModel.isNeedOfLaptop()) {
            decrementMetric(MetricLaptop.class, metricLaptopRepository, metricLaptopRepository::findByLaptop,
                    String.valueOf(oldModel.isNeedOfLaptop()));

            incrementMetric(MetricLaptop.class, metricLaptopRepository, metricLaptopRepository::findByLaptop,
                    MetricLaptop::setLaptop, String.valueOf(newModel.isNeedOfLaptop()));
        }
    }

    @RabbitListener(queues = {"${queue.request.delete.name}"})
    public void receiveMessageDeleteRequest(RequestModel requestModel) {
        log.info("delete: " + requestModel);

        //calc year based metric
        decrementYearMetric(requestModel);

        decrementMetric(MetricCity.class, metricCityRepository, metricCityRepository::findByCity,
                requestModel.getCityName());

        decrementMetric(MetricCountry.class, metricCountryRepository, metricCountryRepository::findByCountry,
                requestModel.getCountryName());

        decrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                requestModel.getStatus().name());


        decrementMetric(MetricTransportationMean.class, transportationMeanRepository,
                transportationMeanRepository::findByTransportationMean,
                String.valueOf(requestModel.getTransportationMeanName()));

        long daysDifference = DAYS.between(requestModel.getStartDate(), requestModel.getEndDate());
        if (daysDifference > 31) {
            daysDifference = 31;
        }


        decrementMetric(MetricDuration.class, metricDurationRepository, metricDurationRepository::findByDuration,
                String.valueOf(daysDifference));

        decrementMetric(MetricLaptop.class, metricLaptopRepository, metricLaptopRepository::findByLaptop,
                String.valueOf(requestModel.isNeedOfLaptop()));

    }

    @RabbitListener(queues = {"${queue.request.accept.name}"})
    public void receiveMessageAcceptRequest(RequestModel requestModel) {
        log.info("accept: " + requestModel);

        decrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                Status.OPEN.name());

        incrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                MetricStatus::setStatus, Status.ACCEPTED.name());
    }

    @RabbitListener(queues = {"${queue.request.reject.name}"})
    public void receiveMessageRejectRequest(RequestModel requestModel) {
        log.info("reject: " + requestModel);

        decrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                Status.OPEN.name());

        incrementMetric(MetricStatus.class, metricStatusRepository, metricStatusRepository::findByStatus,
                MetricStatus::setStatus, Status.REJECTED.name());

    }

    private <T extends BaseMetric> void incrementMetric(Class<T> type,
                                                        MongoRepository repo,
                                                        Function<String, Optional<T>> functionFind,
                                                        BiConsumer<T, String> functionSetter,
                                                        String findBy) {
        Optional<T> optionalObj = functionFind.apply(findBy);

        if (!optionalObj.isPresent()) {
            try {
                T newObj = type.getDeclaredConstructor().newInstance();
                newObj.setNumber(1L);
                functionSetter.accept(newObj, findBy);
                repo.save(newObj);
            } catch (Exception e) {
                log.error("cannot create object of type {}", type.getName());
            }
        } else {
            T metric = optionalObj.get();
            metric.setNumber(metric.getNumber() + 1);
            repo.save(metric);
        }
    }

    private <T extends BaseMetric> void decrementMetric(Class<T> type,
                                                        MongoRepository repo,
                                                        Function<String, Optional<T>> functionFind,
                                                        String findBy) {
        Optional<T> optionalObj = functionFind.apply(findBy);

        if (optionalObj.isPresent()) {
            try {
                T newObj = optionalObj.get();
                newObj.setNumber(newObj.getNumber() - 1);
                repo.save(newObj);
            } catch (Exception e) {
                log.error("cannot create object of type {}", type.getName());
            }
        }
    }

    private void incrementYearMetric(RequestModel requestModel) {
        LocalDate startDate = requestModel.getStartDate();
        String year = String.valueOf(startDate.getYear());
        String month = String.valueOf(startDate.getMonth().getValue());

        Optional<MetricYear> byYear = metricYearRepository.findByYear(year);

        Map<String, Long> yearMap;
        if (!byYear.isPresent()) {
            yearMap = new HashMap<>();
            yearMap.put(month, 1L);
            metricYearRepository.save(new MetricYear(year, yearMap));
        } else {
            MetricYear metricYear = byYear.get();
            yearMap = metricYear.getNumber();

            Long monthCounter = yearMap.get(month);
            if (monthCounter == null) {
                yearMap.put(month, 1L);
            } else {
                //increment counter
                yearMap.put(month, monthCounter + 1);
            }

            metricYearRepository.save(metricYear);
        }
    }

    private void decrementYearMetric(RequestModel requestModel) {
        LocalDate startDate = requestModel.getStartDate();
        String year = String.valueOf(startDate.getYear());
        String month = String.valueOf(startDate.getMonth().getValue());

        Optional<MetricYear> byYear = metricYearRepository.findByYear(year);

        if (byYear.isPresent()) {
            MetricYear metricYear = byYear.get();
            Map<String, Long> yearMap = metricYear.getNumber();

            Long monthCounter = yearMap.get(month);

            if (monthCounter != null) {
                //decrement counter
                yearMap.put(month, monthCounter - 1);
            }

            metricYearRepository.save(metricYear);
        }
    }
}
