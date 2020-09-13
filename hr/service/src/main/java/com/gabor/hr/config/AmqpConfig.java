package com.gabor.hr.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:amqp.properties")
public class AmqpConfig {
    private static final boolean DURABLE = false;

    @Value("${topic.name}")
    private String topicName;

    @Value("${queue.request.save.name}")
    private String saveQueueRoute;

    @Value("${queue.request.update.name}")
    private String updateQueueRoute;

    @Value("${queue.request.delete.name}")
    private String deleteQueueRoute;

    @Value("${queue.request.accept.name}")
    private String acceptQueueRoute;

    @Value("${queue.request.reject.name}")
    private String rejectQueueRoute;

    @Value("${queue.notification.accept.name}")
    private String acceptNotificationQueueRoute;

    @Value("${queue.notification.reject.name}")
    private String rejectNotificationQueueRoute;

    @Value("${queue.timecard.name}")
    private String timecardRoute;

    @Value("${queue.command.name}")
    private String commandRoute;

    @Value("${queue.request.accept.bind.pattern}")
    private String requestAcceptBindPattern;

    @Value("${queue.request.reject.bind.pattern}")
    private String requestRejectBindPattern;

    @Bean
    public Declarables topicBindings() {
        Queue topicQueue1 = new Queue(saveQueueRoute, DURABLE);
        Queue topicQueue2 = new Queue(updateQueueRoute, DURABLE);
        Queue topicQueue3 = new Queue(deleteQueueRoute, DURABLE);
        Queue topicQueue4 = new Queue(acceptQueueRoute, DURABLE);
        Queue topicQueue5 = new Queue(rejectQueueRoute, DURABLE);
        Queue topicQueue6 = new Queue(acceptNotificationQueueRoute, DURABLE);
        Queue topicQueue7 = new Queue(rejectNotificationQueueRoute, DURABLE);
        Queue topicQueue8 = new Queue(timecardRoute, DURABLE);
        Queue topicQueue9 = new Queue(commandRoute, DURABLE);

        TopicExchange topicExchange = new TopicExchange(topicName, DURABLE, false);

        return new Declarables(topicQueue1, topicQueue2, topicQueue3,
                topicQueue4, topicQueue5, topicQueue6, topicQueue7,
                topicQueue8, topicQueue9,
                topicExchange, BindingBuilder
                .bind(topicQueue1)
                .to(topicExchange)
                .with(saveQueueRoute), BindingBuilder
                .bind(topicQueue2)
                .to(topicExchange)
                .with(updateQueueRoute), BindingBuilder
                .bind(topicQueue3)
                .to(topicExchange)
                .with(deleteQueueRoute), BindingBuilder
                .bind(topicQueue4)
                .to(topicExchange)
                .with(requestAcceptBindPattern), BindingBuilder
                .bind(topicQueue5)
                .to(topicExchange)
                .with(requestRejectBindPattern), BindingBuilder
                .bind(topicQueue6)
                .to(topicExchange)
                .with(requestAcceptBindPattern), BindingBuilder
                .bind(topicQueue7)
                .to(topicExchange)
                .with(requestRejectBindPattern), BindingBuilder
                .bind(topicQueue8)
                .to(topicExchange)
                .with(timecardRoute), BindingBuilder
                .bind(topicQueue9)
                .to(topicExchange)
                .with(commandRoute));
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
