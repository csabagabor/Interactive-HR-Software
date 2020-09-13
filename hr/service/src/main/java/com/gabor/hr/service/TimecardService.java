package com.gabor.hr.service;

import com.gabor.hr.exception.ResourceNotFoundException;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.TimeCard;
import com.gabor.hr.model.User;
import com.gabor.hr.model.WorkedTask;
import com.gabor.hr.repository.TimeCardRepository;
import com.gabor.hr.repository.UserRepository;
import com.gabor.hr.repository.WorkedTaskRepository;
import com.gabor.hr.service.dto.ProjectDto;
import com.gabor.hr.service.dto.TimeCardDto;
import com.gabor.hr.service.dto.WorkedTaskDto;
import com.gabor.model.dto.RequestModel;
import com.gabor.model.dto.TimeCardRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimecardService extends CrudService<TimeCard, TimeCardDto, TimeCardRepository> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    public Optional<TimeCardDto> getCurrentTimeCard(String currentUserEmail) {
        User byEmail = userRepository.findByEmail(currentUserEmail);
        LocalDate now = LocalDate.now();
        Optional<TimeCard> byYearAndMonth = repository.findByYearAndMonthAndUser(now.getYear(), now.getMonth(), byEmail);

        if (!byYearAndMonth.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(modelMapper.map(byYearAndMonth.get(), TimeCardDto.class));
    }

    public TimeCardDto updateWithTasks(TimeCardDto currentTimeCardDto) {
        TimeCard timeCard = repository.findById(currentTimeCardDto.getId()).get();

        //make Status Open
        timeCard.setStatus(Status.OPEN);
        return modelMapper.map(repository.save(timeCard), TimeCardDto.class);
    }

    public List<TimeCardDto> findAllByStatus(Status status) {
        return repository.findAllByStatus(status).stream()
                .map(obj -> modelMapper.map(obj, TimeCardDto.class))
                .collect(Collectors.toList());
    }

    public void accept(Long id) {
        Optional<TimeCard> byId = repository.findById(id);

        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("timecard", "id", id);
        }

        TimeCard timeCard = byId.get();

        timeCard.setStatus(Status.ACCEPTED);
        repository.save(timeCard);

        messageService.sendTimecard(new TimeCardRequestModel(com.gabor.model.dto.Status.ACCEPTED, timeCard.getUser().getEmail()));
    }

    public void reject(Long id) {
        Optional<TimeCard> byId = repository.findById(id);

        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("timecard", "id", id);
        }

        TimeCard timeCard = byId.get();
        timeCard.setStatus(Status.REJECTED);
        repository.save(timeCard);

        messageService.sendTimecard(new TimeCardRequestModel(com.gabor.model.dto.Status.REJECTED, timeCard.getUser().getEmail()));
    }

    public List<WorkedTaskDto> findTasksById(Long id) {
        Optional<TimeCard> byId = repository.findById(id);

        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("timecard", "id", id);
        }

        return byId.get().getWorkedTasks().stream()
                .map(obj -> modelMapper.map(obj, WorkedTaskDto.class))
                .collect(Collectors.toList());
    }


    public Map<String, Integer> findTasksByIdPayroll(Long id) {
        Optional<TimeCard> byId = repository.findById(id);

        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("timecard", "id", id);
        }

        Map<String, Integer> durations = new HashMap<>();
        for (WorkedTask workedTask : byId.get().getWorkedTasks()) {
            String identifier = workedTask.getTask().getIdentifier();

            durations.merge(identifier, workedTask.getDuration(), Integer::sum);
        }

        return durations;
    }

    public List<TimeCardDto> findAllPastMonth() {
        LocalDate localDatePastMonth = LocalDate.now().minusMonths(1);

        return repository.findAllByYearAndMonthAndStatus(localDatePastMonth.getYear(), localDatePastMonth.getMonth(), Status.ACCEPTED)
                .stream()
                .map(obj -> modelMapper.map(obj, TimeCardDto.class))
                .collect(Collectors.toList());
    }
}
