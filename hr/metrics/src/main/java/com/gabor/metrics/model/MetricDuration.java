package com.gabor.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "duration_metric")
public class MetricDuration extends BaseMetric {

    @Indexed
    private String duration;

    public MetricDuration(String duration, Long number) {
        this.number = number;
        this.duration = duration;
    }
}