package com.gabor.hr.service.dto;

import com.gabor.hr.model.BaseModel;
import com.gabor.hr.model.Project;
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
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto extends BaseDto {

    @NotBlank(message = "Identifier must not be empty")
    private String identifier;

    @NotBlank(message = "Description must not be empty")
    private String description;

    private Long projectId;

}
