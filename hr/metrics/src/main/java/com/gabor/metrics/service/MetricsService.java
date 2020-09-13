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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class MetricsService {

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

    public Map<String, Map<String, Long>> getMetricsWithRequestsPerMonth() {
        List<MetricYear> all = metricYearRepository.findAll();
        Map<String, Map<String, Long>> metrics = new HashMap<>();

        for (MetricYear metricYear : all) {
            metrics.put(metricYear.getYear(), metricYear.getNumber());
        }

        return metrics;
    }

    public Map<String, Long> getMetricsWithRequestsPerCountry() {
        return convertListToMap(metricCountryRepository.findAll(), MetricCountry::getCountry);
    }

    public Map<String, Long> getMetricsWithRequestsPerTransportationMean() {
        return convertListToMap(transportationMeanRepository.findAll(), MetricTransportationMean::getTransportationMean);
    }

    public Map<String, Long> getMetricsWithRequestsPerStatus() {
        return convertListToMap(metricStatusRepository.findAll(), MetricStatus::getStatus);
    }

    public Map<String, Long> getMetricsWithRequestsPerLaptop() {
        return convertListToMap(metricLaptopRepository.findAll(), MetricLaptop::getLaptop);
    }

    public Map<String, Long> getMetricsWithRequestsPerCity() {
        return convertListToMap(metricCityRepository.findAll(), MetricCity::getCity);
    }

    public Map<String, Long> getMetricsWithRequestsPerDuration() {
        return convertListToMap(metricDurationRepository.findAll(), MetricDuration::getDuration);
    }

    private <T, E extends BaseMetric> Map<T, Long> convertListToMap(List<E> all, Function<E, T> func) {
        Map<T, Long> map = new HashMap<>();

        for (E e : all) {
            //only if not zero
            if (e.getNumber() > 0) {
                map.put(func.apply(e), e.getNumber());
            }
        }

        return map;
    }
}
