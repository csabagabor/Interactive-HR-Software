package com.gabor.notification.service;

import com.gabor.model.dto.CommandModel;
import com.gabor.model.dto.RequestModel;
import com.gabor.model.dto.TimeCardRequestModel;
import com.gabor.notification.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = {"${queue.notification.accept.name}"})
    public void receiveMessageAcceptRequest(RequestModel requestModel) {
        log.info("accept: " + requestModel);

        String userEmail = requestModel.getUserEmail();
        String cityName = requestModel.getCityName();

        messagingTemplate.convertAndSendToUser(userEmail, "/queue/reply",
                new ResponseMessage("Request to " + cityName + " accepted!"));
    }

    @RabbitListener(queues = {"${queue.notification.reject.name}"})
    public void receiveMessageRejectRequest(RequestModel requestModel) {
        log.info("reject: " + requestModel);

        String userEmail = requestModel.getUserEmail();
        String cityName = requestModel.getCityName();

        messagingTemplate.convertAndSendToUser(userEmail, "/queue/reply",
                new ResponseMessage("Request to " + cityName + " rejected!"));
    }

    //timecard
    @RabbitListener(queues = {"${queue.timecard.name}"})
    public void receiveTimcard(TimeCardRequestModel requestModel) {
        log.info("accept: " + requestModel);

        String userEmail = requestModel.getUserEmail();

        messagingTemplate.convertAndSendToUser(userEmail, "/queue/reply",
                new ResponseMessage("Timecard " + requestModel.getStatus().toString()));
    }

    //command
    @RabbitListener(queues = {"${queue.command.name}"})
    public void recieveCommand(CommandModel requestModel) {
        log.info("accept: " + requestModel);

        String userEmail = requestModel.getUserEmail();

        messagingTemplate.convertAndSendToUser(userEmail, "/queue/reply",
                new ResponseMessage(requestModel.getCommand()));
    }
}
