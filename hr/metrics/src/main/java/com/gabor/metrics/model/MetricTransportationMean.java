package com.gabor.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transportationMean_metric")
public class MetricTransportationMean extends BaseMetric {

    @Indexed
    private String transportationMean;

    public MetricTransportationMean(String transportationMean, Long number) {
        this.number = number;
        this.transportationMean = transportationMean;
    }
}