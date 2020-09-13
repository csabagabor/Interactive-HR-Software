package com.gabor.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "country_metric")
public class MetricCountry extends BaseMetric {

    @Indexed
    private String country;

    public MetricCountry(String country, Long number) {
        this.number = number;
        this.country = country;
    }
}