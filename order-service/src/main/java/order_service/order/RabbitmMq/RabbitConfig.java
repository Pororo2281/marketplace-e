package order_service.order.RabbitmMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.queue.order}")
    private String orderQueueName;

    @Value("${rabbitmq.queue.dead}")
    private String dlqQueueName;

    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "dlq.exchange");
        args.put("x-dead-letter-routing-key", "dlq");

        return new Queue(orderQueueName, false, false, false, args);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(dlqQueueName);
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange("dlq.exchange");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(dlqExchange())
                .with("dlq");
    }


    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("payment.exchange");
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange paymentExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(paymentExchange)
                .with("payment.succeeded");
    }

}
