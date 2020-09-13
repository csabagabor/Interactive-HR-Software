package com.gabor.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "status_metric")
public class MetricStatus extends BaseMetric {

    @Indexed
    private String status;

    public MetricStatus(String status, Long number) {
        this.number = number;
        this.status = status;
    }
}