package com.gabor.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "laptop_metric")
public class MetricLaptop extends BaseMetric {

    @Indexed
    private String laptop;

    public MetricLaptop(String laptop, Long number) {
        this.number = number;
        this.laptop = laptop;
    }
}