package com.gabor.hr.service;

import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class EmailService {

    @Autowired
    private SendGrid sendGridClient;

    @Async
    public void sendSimpleMessage(
            String to, String subject, String html) {

        Mail mail = new Mail(new Email(yourEmail), subject, new Email(to),
                new Content("text/html", html));
        mail.setReplyTo(new Email(to));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGridClient.api(request);
        } catch (IOException ex) {
            log.warn(ex.getMessage());
        }
    }
}
