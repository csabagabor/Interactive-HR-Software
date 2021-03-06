package com.gabor.hr.service.dto;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto extends BaseDto {
    @NotBlank(message = "Name must not be empty")
    @Size(max = Constants.COUNTRY_NAME_LENGTH_MAX, message = "Country Name too long")
    private String name;
}
