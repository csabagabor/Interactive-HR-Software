package com.gabor.hr.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import com.gabor.model.config.Constants;
import com.gabor.hr.model.Status;


import com.gabor.hr.service.dto.validator.DateInFuture;
import com.gabor.hr.service.dto.validator.EndDateAfterStartDate;
import com.gabor.model.dto.serialization.LocalDateDeserializer;
import com.gabor.model.dto.serialization.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EndDateAfterStartDate
@DateInFuture
public class RequestDto extends BaseDto {
    @NotNull(message = "startDate must not be empty")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NonNull
    private LocalDate startDate;

    @NotNull(message = "endDate must not be empty")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NonNull
    private LocalDate endDate;

    @NonNull
    private boolean needOfPhone;
    @NonNull
    private boolean needOfVpn;
    @NonNull
    private boolean needOfLaptop;
    @NonNull
    private String details;

    @Size(max = Constants.USER_EMAIL_LENGTH_MAX, message = "Email too long")
    private String userEmail;

    @NotBlank(message = "Transportation Mean must not be empty")
    @Size(max = Constants.TRANSPORTATION_MEAN_LENGTH_MAX, message = "Transportation mean Name too long")
    @NonNull
    private String transportationMeanName;

    @NotBlank(message = "Country must not be empty")
    @Size(max = Constants.COUNTRY_NAME_LENGTH_MAX, message = "Country Name too long")
    @NonNull
    private String countryName;

    @NotBlank(message = "City must not be empty")
    @Size(max = Constants.CITY_NAME_LENGTH_MAX, message = "City Name too long")
    @NonNull
    private String cityName;
    @NonNull
    private Status status;

    private String hidden;

}
