package com.gabor.hr.config;

import com.gabor.hr.exception.ResourceNotFoundException;
import com.gabor.hr.model.*;
import com.gabor.hr.repository.*;
import com.gabor.hr.service.dto.CityDto;
import com.gabor.hr.service.dto.RequestDto;
import com.gabor.hr.service.dto.TaskDto;
import com.gabor.hr.service.dto.TimeCardDto;
import com.gabor.hr.service.dto.WorkedTaskDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class MapperConfig {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TransportationMeanRepository transportationMeanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, String> emptyConverter = MappingContext::getSource;

        Converter<String, Country> countryConverter = context -> {
            String src = context.getSource();
            Country country = countryRepository.findByName(src);

            if (country == null) {
                throw new ResourceNotFoundException("country", "name", src);
            }

            return country;
        };

        //city name can be duplicated
        Converter<RequestDto, City> cityConverter = context -> {
            RequestDto src = context.getSource();

            Country country = countryRepository.findByName(src.getCountryName());

            if (country == null) {
                throw new ResourceNotFoundException("country", "name", src);
            }

            City city = cityRepository.findByNameAndCountry(src.getCityName(), country);

            if (city == null) {
                throw new ResourceNotFoundException("city", "name", src);
            }

            return city;
        };

        Converter<String, TransportationMean> transportationMeanConverter = context -> {
            String src = context.getSource();
            TransportationMean transportationMean = transportationMeanRepository.findByName(src);

            if (transportationMean == null) {
                throw new ResourceNotFoundException("transportationMean", "name", src);
            }

            return transportationMean;
        };

        Converter<Long, Project> projectConverter = context -> {
            Long src = context.getSource();
            Optional<Project> project = projectRepository.findById(src);

            if (!project.isPresent()) {
                throw new ResourceNotFoundException("project", "title", src);
            }

            return project.get();
        };

        Converter<String, Task> taskConverter = context -> {
            String src = context.getSource();
            Optional<Task> task = taskRepository.findByIdentifier(src);

            if (!task.isPresent()) {
                throw new ResourceNotFoundException("task", "identifier", src);
            }

            return task.get();
        };

        Converter<String, User> userConverter = context -> {
            String src = context.getSource();
            User user = userRepository.findByEmail(src);

            if (user == null) {
                throw new ResourceNotFoundException("user", "email", src);
            }

            return user;
        };

        PropertyMap<CityDto, City> cityDtoMap = new PropertyMap<CityDto, City>() {
            protected void configure() {
                using(countryConverter).map(source.getCountryName()).setCountry(null);
            }
        };

        PropertyMap<RequestDto, Request> requestDtoMap = new PropertyMap<RequestDto, Request>() {
            protected void configure() {
                using(cityConverter).map(source).setCity(null);
                using(transportationMeanConverter).map(source.getTransportationMeanName()).setTransportationMean(null);
                using(userConverter).map(source.getUserEmail()).setUser(null);
            }
        };

        PropertyMap<Request, RequestDto> requestMap = new PropertyMap<Request, RequestDto>() {
            protected void configure() {
                using(emptyConverter).map(source.getCity().getCountry().getName()).setCountryName(null);
            }
        };

        PropertyMap<TaskDto, Task> projectMap = new PropertyMap<TaskDto, Task>() {
            protected void configure() {
                using(projectConverter).map(source.getProjectId()).setProject(null);
            }
        };

        PropertyMap<WorkedTaskDto, WorkedTask>  workedTaskMap = new PropertyMap<WorkedTaskDto, WorkedTask>() {
            protected void configure() {
                using(taskConverter).map(source.getTaskIdentifier()).setTask(null);
            }
        };


        PropertyMap<TimeCardDto, TimeCard>  timecardMap = new PropertyMap<TimeCardDto, TimeCard>() {
            protected void configure() {
                using(userConverter).map(source.getUserEmail()).setUser(null);
            }
        };

        modelMapper.addMappings(cityDtoMap);
        modelMapper.addMappings(requestDtoMap);
        modelMapper.addMappings(requestMap);
        modelMapper.addMappings(projectMap);
        modelMapper.addMappings(workedTaskMap);
        modelMapper.addMappings(timecardMap);
        return modelMapper;
    }
}
