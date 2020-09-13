package com.gabor.hr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "worked_task")
public class WorkedTask extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column
    private LocalDate date;

    @Column
    private Integer duration;

    @ManyToOne
    @JoinColumn(name="timecard_id")
    private TimeCard timeCard;

}
