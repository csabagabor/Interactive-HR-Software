package com.gabor.hr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timecard")
public class TimeCard extends BaseModel {

    @Column
    private int year;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private Month month;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Status status;

    @OneToMany(mappedBy="timeCard")
    private List<WorkedTask> workedTasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}