package product_service.demo.RabbitMq;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
public class RabbitDeclareQueue {

    private final RabbitAdmin rabbitAdmin;
    private final Queue queue;

    public RabbitDeclareQueue(RabbitAdmin rabbitAdmin, Queue queue) {
        this.rabbitAdmin = rabbitAdmin;
        this.queue = queue;
    }

    @PostConstruct
    private void declareQueue(){
        rabbitAdmin.declareQueue(queue);
    }

}
