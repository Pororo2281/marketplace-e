package com.notification.notification.RabbitMq;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RabbitDeclareQueue {

    private final RabbitAdmin rabbitAdmin;
    private final Queue orderCreatedQueue;
    private final Queue orderPaidQueueName;
    private final Queue userQueue;

    public RabbitDeclareQueue(RabbitAdmin rabbitAdmin,
                              @Qualifier("orderCreatedQueue") Queue orderCreatedQueue,
                              @Qualifier("orderPaidQueue")Queue orderPaidQueueName,
                              @Qualifier("userQueue") Queue userQueue) {
        this.rabbitAdmin = rabbitAdmin;
        this.orderCreatedQueue = orderCreatedQueue;
        this.orderPaidQueueName = orderPaidQueueName;
        this.userQueue = userQueue;
    }

    @PostConstruct
    private void declareQueue(){
        rabbitAdmin.declareQueue(orderCreatedQueue);
        rabbitAdmin.declareQueue(orderPaidQueueName);
        rabbitAdmin.declareQueue(userQueue);
    }

}
