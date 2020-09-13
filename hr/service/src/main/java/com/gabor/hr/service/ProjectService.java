package com.gabor.hr.service;

import com.gabor.hr.model.Country;
import com.gabor.hr.model.Project;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.Task;
import com.gabor.hr.repository.CountryRepository;
import com.gabor.hr.repository.ProjectRepository;
import com.gabor.hr.service.dto.CountryDto;
import com.gabor.hr.service.dto.ProjectDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService extends CrudService<Project, ProjectDto, ProjectRepository> {

}
