package com.gabor.hr.repository;


import com.gabor.hr.model.City;
import com.gabor.hr.model.Country;
import com.gabor.hr.model.Project;
import com.gabor.hr.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByTitle(String title);
}
