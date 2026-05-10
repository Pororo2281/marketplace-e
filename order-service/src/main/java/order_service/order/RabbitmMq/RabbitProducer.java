package order_service.order.RabbitmMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import order_service.order.Response.OrderMailResponse;
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

    public void sendOrderCreated(OrderMailResponse response) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.created",
                json
        );
    }

    public void sendOrderPaid(OrderMailResponse response) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.paid",
                json
        );
    }

}
