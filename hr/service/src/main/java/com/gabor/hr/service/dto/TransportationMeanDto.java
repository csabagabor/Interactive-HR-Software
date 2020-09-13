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
public class TransportationMeanDto extends BaseDto {
    @NotBlank(message = "Name must not be empty")
    @Size(max = Constants.TRANSPORTATION_MEAN_LENGTH_MAX, message = "Transportation Mean Name too long")
    private String name;
}
