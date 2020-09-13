package com.gabor.hr.model;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "country")
public class Country extends BaseModel {

    @Column(length = Constants.COUNTRY_NAME_LENGTH_MAX, unique = true)
    private String name;

}
