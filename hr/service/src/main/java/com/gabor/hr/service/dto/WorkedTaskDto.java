package com.gabor.hr.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gabor.hr.model.BaseModel;
import com.gabor.hr.model.Project;
import com.gabor.model.config.Constants;
import com.gabor.model.dto.serialization.LocalDateDeserializer;
import com.gabor.model.dto.serialization.LocalDateSerializer;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkedTaskDto extends BaseDto {

    @NotBlank(message = "Task Identifier must not be empty")
    private String taskIdentifier;

    @NotNull(message = "Date must not be empty")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @NotNull(message = "Duration must not be empty")
    private Integer duration;

    @Size(max = Constants.USER_EMAIL_LENGTH_MAX, message = "Email too long")
    private String userEmail;

    private Long timeCardId;

    private String taskProjectTitle;

    private String hidden;
}
