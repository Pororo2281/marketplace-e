package com.notification.notification.RabbitMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.queue.order-created}")
    private String orderCreatedQueueName;

    @Value("${rabbitmq.queue.order-paid}")
    private String orderPaidQueueName;

    @Value("${rabbitmq.queue.user-activity}")
    private String userQueue;


    @Bean
    public CachingConnectionFactory connectionFactory() {
        String host = System.getenv().getOrDefault("SPRING_RABBITMQ_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("SPRING_RABBITMQ_PORT", "5672"));
        CachingConnectionFactory factory = new CachingConnectionFactory(host, port);

        factory.setUsername(user);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange("user.exchange");
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(orderCreatedQueueName, false);
    }

    @Bean
    public Queue orderPaidQueue() {
        return new Queue(orderPaidQueueName, false);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueue, false);
    }

    @Bean
    public Binding orderPaidBinding(Queue orderPaidQueue,
                                       DirectExchange orderExchange) {
        return BindingBuilder
                .bind(orderPaidQueue)
                .to(orderExchange)
                .with("order.paid");
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue,
                                DirectExchange orderExchange) {
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderExchange)
                .with("order.created");
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Binding userRegisterBinding(Queue userQueue,
                                       DirectExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with("user.created");
    }

    @Bean
    public Binding userLoginBinding(Queue userQueue,
                               DirectExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with("user.login");
    }

    @Bean
    public Binding userResetPasswordBinding(Queue userQueue,
                                    DirectExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with("user.reset_password_requested");
    }

    @Bean
    public Binding userChangedPasswordBinding(Queue userQueue,
                                            DirectExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with("user.password_changed");
    }
}
