package com.gabor.hr.service.dto;

import com.gabor.hr.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeCardDto extends BaseDto {

    private int year;

    private Month month;

    private Status status;

    private String userEmail;
}