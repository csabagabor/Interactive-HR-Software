package com.gabor.hr.model;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "transportation_mean")
public class TransportationMean extends BaseModel {

    @Column(length = Constants.TRANSPORTATION_MEAN_LENGTH_MAX, unique = true)
    private String name;
}
