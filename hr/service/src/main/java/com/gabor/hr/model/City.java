package com.gabor.hr.model;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "country_id"}))//same city name can appear in multiple countries
public class City extends BaseModel {

    @Column(length = Constants.CITY_NAME_LENGTH_MAX)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Country country;
}
