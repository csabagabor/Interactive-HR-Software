package com.gabor.hr.repository;


import com.gabor.hr.model.City;
import com.gabor.hr.model.Country;
import com.gabor.hr.model.Project;
import com.gabor.hr.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectId(Long id);
    List<Task> findAllByProjectTitle(String title);
    Optional<Task> findByIdentifier(String identifier);
}
