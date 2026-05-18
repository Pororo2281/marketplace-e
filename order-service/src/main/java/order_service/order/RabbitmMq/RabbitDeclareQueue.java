package order_service.order.RabbitmMq;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RabbitDeclareQueue {
    private final RabbitAdmin rabbitAdmin;
    private final Queue orderQueue;
    private final Queue deadLetterQueue;

    public RabbitDeclareQueue(RabbitAdmin rabbitAdmin,
                              @Qualifier("orderQueue") Queue orderQueue,
                              @Qualifier("deadLetterQueue")Queue deadLetterQueue
                              ) {
        this.deadLetterQueue =deadLetterQueue;
        this.orderQueue = orderQueue;
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    private void declareQueue(){
        rabbitAdmin.declareQueue(orderQueue);
        rabbitAdmin.declareQueue(deadLetterQueue);
    }
}
