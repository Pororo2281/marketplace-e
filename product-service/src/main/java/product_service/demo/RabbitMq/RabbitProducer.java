package product_service.demo.RabbitMq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import product_service.demo.Event.ProductBatchEvent;
import product_service.demo.Event.ProductEvent;

import java.util.List;

@Service
public class RabbitProducer {
    private final ObjectMapper objectMapper;
    private RabbitTemplate rabbitTemplate;

    public RabbitProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendProductEvent(ProductEvent event) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String routingKey = switch (event.getType()) {
            case CREATED -> "product.created";
            case UPDATED -> "product.updated";
            case DELETED -> "product.deleted";
        };
        rabbitTemplate.convertAndSend(
                "product.exchange",
                routingKey,
                json
        );
    }

    public void reindex(ProductBatchEvent productBatchEvent) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(productBatchEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String routingKey = "product.updated";
        rabbitTemplate.convertAndSend(
                "product.exchange",
                routingKey,
                json
        );
    }



}
