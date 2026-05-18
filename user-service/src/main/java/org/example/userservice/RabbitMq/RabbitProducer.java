package org.example.userservice.RabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PrePersist;
import org.example.userservice.Event.UserEvent;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserActivity(UserEvent userEvent){
        String json = null;
        json = objectMapper.writeValueAsString(userEvent);
        rabbitTemplate.convertAndSend(
                "user.exchange",
                "user." + userEvent.getEventType().name().toLowerCase(),
                json
        );
    }

}
