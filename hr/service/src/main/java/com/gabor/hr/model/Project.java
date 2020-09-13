package com.gabor.hr.model;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project extends BaseModel{

    @Column(length = Constants.PROJECT_TITLE_NAME_LENGTH_MAX, unique = true)
    private String title;

    @Column(length = Constants.PROJECT_DESCRIPTION_NAME_LENGTH_MAX)
    private String description;

}
