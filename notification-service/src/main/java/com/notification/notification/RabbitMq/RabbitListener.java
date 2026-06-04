package com.notification.notification.RabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notification.Event.UserEvent;
import com.notification.notification.Event.OrderEvent;
import com.notification.notification.Exception.InvalidMessageException;
import com.notification.notification.Service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class RabbitListener {


    private final ObjectMapper objectMapper;
    private final EmailService emailService;


    public RabbitListener(ObjectMapper objectMapper, EmailService emailService ) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"${rabbitmq.queue.order-created}"})
    public void receiveOrderCreated(String json){
        try{
            OrderEvent orderEvent = objectMapper.readValue(json, OrderEvent.class);
            emailService.sendMessage(orderEvent.getEmail(),orderEvent);
        }catch (JsonProcessingException e) {
            throw new InvalidMessageException("Failed to deserialize",e);
        }
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"${rabbitmq.queue.order-paid}"})
    public void receiveOrderPaid(String json){
        try{
            OrderEvent orderEvent = objectMapper.readValue(json, OrderEvent.class);
            emailService.sendMessage(orderEvent.getEmail(),orderEvent);
        }catch (JsonProcessingException e) {
            throw new InvalidMessageException("Failed to deserialize",e);
        }
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"${rabbitmq.queue.user-activity}"})
    public void receiveUserActivity(String json){
        try{
            UserEvent userEvent = objectMapper.readValue(json, UserEvent.class);
            emailService.sendMessage(userEvent.getEmail(),userEvent);

        }catch (JsonProcessingException e) {
            throw new InvalidMessageException("Failed to deserialize",e);
        }
    }
}
