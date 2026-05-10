package payment_service.payment_service.RabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import payment_service.payment_service.Response.PaymentSucceededEvent;

@Service
public class RabbitProducer {


    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPaymentEvent(PaymentSucceededEvent request){
        String json = null;
        try {
            json = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.succeeded",
                json);
    }

}
