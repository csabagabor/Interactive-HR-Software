package com.gabor.hr.repository;


import com.gabor.hr.model.City;
import com.gabor.hr.model.Country;
import com.gabor.hr.model.User;
import com.gabor.hr.model.WorkedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkedTaskRepository extends JpaRepository<WorkedTask, Long> {
}
