package com.marketplace.review_service.RabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.review_service.Event.ReviewEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendReviewEvent(ReviewEvent event) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        rabbitTemplate.convertAndSend(
                "review.exchange",
                "review.event",
                json
        );
    }
}
