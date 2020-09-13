package com.gabor.metrics.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class BaseMetric {
    @Getter
    @Setter
    @Id
    private String id;

    @Getter
    @Setter
    protected Long number;
}
