package com.gabor.hr.service;

import com.gabor.hr.service.dto.RequestDto;
import com.gabor.model.dto.CommandModel;
import com.gabor.model.dto.RequestModel;
import com.gabor.model.dto.RequestUpdateModel;
import com.gabor.model.dto.TimeCardRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Value("${topic.name}")
    private String topicName;

    @Value("${queue.request.save.name}")
    private String saveQueue;

    @Value("${queue.request.update.name}")
    private String updateQueue;

    @Value("${queue.request.delete.name}")
    private String deleteQueue;

    @Value("${queue.request.accept.name}")
    private String acceptQueueRoute;

    @Value("${queue.request.reject.name}")
    private String rejectQueueRoute;

    @Value("${queue.notification.accept.name}")
    private String acceptQueueRoute2222;

    @Value("${queue.timecard.name}")
    private String timecardRoute;

    @Value("${queue.command.name}")
    private String commandRoute;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSaveRequest(RequestModel requestModel) {
        rabbitTemplate.convertAndSend(topicName,
                saveQueue, requestModel);
    }

    public void sendAcceptRequest(RequestModel requestModel) {
        rabbitTemplate.convertAndSend(topicName,
                acceptQueueRoute, requestModel);
    }

    public void sendRejectRequest(RequestModel requestModel) {
        rabbitTemplate.convertAndSend(topicName,
                rejectQueueRoute, requestModel);
    }

    public void sendDelete(RequestModel requestModel) {
        rabbitTemplate.convertAndSend(topicName,
                deleteQueue, requestModel);
    }

    public void sendUpdate(RequestModel oldRequestModel, RequestModel requestModel) {
        rabbitTemplate.convertAndSend(topicName,
                updateQueue, new RequestUpdateModel(oldRequestModel, requestModel));
    }

    public void sendTimecard(TimeCardRequestModel timeCardRequestModel) {
        rabbitTemplate.convertAndSend(topicName,
                timecardRoute, timeCardRequestModel);
    }

    public void sendCommand(CommandModel commandModel) {
        rabbitTemplate.convertAndSend(topicName,
                commandRoute, commandModel);
    }
}
