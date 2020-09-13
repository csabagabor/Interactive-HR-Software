package com.gabor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateModel {
    private RequestModel oldModel;
    private RequestModel newModel;

}
