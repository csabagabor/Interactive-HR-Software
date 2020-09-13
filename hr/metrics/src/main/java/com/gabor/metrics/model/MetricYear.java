package com.gabor.metrics.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "year_metric")
public class MetricYear {

    @Getter
    @Setter
    @Id
    private String id;

    @Indexed
    private String year;

    private Map<String, Long> number;

    public MetricYear(String year, Map<String, Long> number) {
        this.number = number;
        this.year = year;
    }
}