package com.gabor.hr.model;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task extends BaseModel {

    @Column(length = Constants.TASK_IDENTIFIER_NAME_LENGTH_MAX, unique = true)
    private String identifier;

    @Column(length = Constants.TASK_DESCRIPTION_NAME_LENGTH_MAX)
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)//project is deleted, task is deleted
    private Project project;

}
