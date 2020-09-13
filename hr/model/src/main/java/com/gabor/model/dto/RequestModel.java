package com.gabor.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gabor.model.dto.serialization.LocalDateDeserializer;
import com.gabor.model.dto.serialization.LocalDateSerializer;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestModel {
    @Getter
    @Setter
    private Long id;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    private boolean needOfPhone;

    private boolean needOfVpn;

    private boolean needOfLaptop;

    private String details;

    private String userEmail;

    private String transportationMeanName;

    private String countryName;

    private String cityName;

    private Status status;
}