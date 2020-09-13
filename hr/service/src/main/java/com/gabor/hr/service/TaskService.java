package com.gabor.hr.service;

import com.gabor.hr.model.Project;
import com.gabor.hr.model.Task;
import com.gabor.hr.repository.TaskRepository;
import com.gabor.hr.service.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService extends CrudService<Task, TaskDto, TaskRepository> {

    public List<TaskDto> findTasksByProject(Long id) {
        return repository.findAllByProjectId(id).stream()
                .map(obj -> modelMapper.map(obj, TaskDto.class))
                .collect(Collectors.toList());
    }

    public List<TaskDto> findTasksByProject(String title) {
        return repository.findAllByProjectTitle(title).stream()
                .map(obj -> modelMapper.map(obj, TaskDto.class))
                .collect(Collectors.toList());
    }
}
