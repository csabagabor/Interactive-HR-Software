package com.gabor.hr.repository;


import com.gabor.hr.model.Project;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.TimeCard;
import com.gabor.hr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeCardRepository extends JpaRepository<TimeCard, Long> {
    Optional<TimeCard> findByYearAndMonthAndUser(int year, Month month, User user);
    List<TimeCard> findAllByStatus(Status status);
    List<TimeCard> findAllByYearAndMonthAndStatus(int year, Month month, Status status);
}
