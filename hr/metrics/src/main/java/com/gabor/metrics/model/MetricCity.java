package com.gabor.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "city_metric")
public class MetricCity extends BaseMetric {

    @Indexed
    private String city;

    public MetricCity(String city, Long number) {
        this.number = number;
        this.city = city;
    }
}