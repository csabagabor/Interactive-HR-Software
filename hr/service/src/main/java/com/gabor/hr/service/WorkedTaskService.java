package com.gabor.hr.service;

import com.gabor.hr.exception.StatusMismathException;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.TimeCard;
import com.gabor.hr.model.User;
import com.gabor.hr.model.WorkedTask;
import com.gabor.hr.repository.TimeCardRepository;
import com.gabor.hr.repository.UserRepository;
import com.gabor.hr.repository.WorkedTaskRepository;
import com.gabor.hr.service.dto.WorkedTaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkedTaskService extends CrudService<WorkedTask, WorkedTaskDto, WorkedTaskRepository> {

    @Autowired
    private TimeCardRepository timeCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WorkedTaskDto update(WorkedTaskDto obj) {
        WorkedTaskDto byId = findById(obj.getId());

        Long timeCardId = byId.getTimeCardId();
        TimeCard timeCard = timeCardRepository.findById(timeCardId).get();
        if (!Status.NOT_SENT.equals(timeCard.getStatus()) && !Status.REJECTED.equals(timeCard.getStatus())) {
            throw new StatusMismathException("Timecard is not OPEN");
        }

        //UI does not send it
        obj.setTimeCardId(byId.getTimeCardId());
        return super.update(obj);
    }

    @Override
    public void delete(Long id) {
        //checks if it exists
        findById(id);

        WorkedTaskDto byId = findById(id);
        Long timeCardId = byId.getTimeCardId();
        TimeCard timeCard = timeCardRepository.findById(timeCardId).get();
        if (!Status.NOT_SENT.equals(timeCard.getStatus()) && !Status.REJECTED.equals(timeCard.getStatus())) {
            throw new StatusMismathException("Timecard is not OPEN");
        }

        super.delete(id);
    }

    @Override
    public WorkedTaskDto save(WorkedTaskDto obj) {
        String currentUserEmail = obj.getUserEmail();
        User byEmail = userRepository.findByEmail(currentUserEmail);

        LocalDate now = LocalDate.now();
        Optional<TimeCard> byYearAndMonth = timeCardRepository.findByYearAndMonthAndUser(now.getYear(), now.getMonth(), byEmail);

        TimeCard timeCard = null;
        if (!byYearAndMonth.isPresent()) {
            //no timecard, create one
            timeCard = new TimeCard(now.getYear(), now.getMonth(), Status.NOT_SENT, new ArrayList<>(), byEmail);
            timeCard = timeCardRepository.save(timeCard);
        } else {
            timeCard = byYearAndMonth.get();
        }

        if (!Status.NOT_SENT.equals(timeCard.getStatus()) && !Status.REJECTED.equals(timeCard.getStatus())) {
            throw new StatusMismathException("Timecard is not OPEN");
        }

        WorkedTask workedTask = modelMapper.map(obj, WorkedTask.class);
        timeCard.getWorkedTasks().add(workedTask);
        workedTask.setTimeCard(timeCard);
        WorkedTask savedObj = repository.save(workedTask);

        return modelMapper.map(savedObj, WorkedTaskDto.class);
    }

    public List<WorkedTaskDto> findAllOwn(String currentUserEmail) {
        User byEmail = userRepository.findByEmail(currentUserEmail);

        LocalDate now = LocalDate.now();
        Optional<TimeCard> byYearAndMonth = timeCardRepository.findByYearAndMonthAndUser(now.getYear(), now.getMonth(), byEmail);

        if (!byYearAndMonth.isPresent()) {
           return new ArrayList<>();
        }

        return byYearAndMonth.get().getWorkedTasks().stream()
                .map(obj -> modelMapper.map(obj, WorkedTaskDto.class))
                .collect(Collectors.toList());
    }
}
