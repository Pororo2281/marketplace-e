package product_service.demo.RabbitMq;

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

    @Value("${rabbitmq.queue.product}")
    private String reviewQueueName;

    @Bean
    public Queue reviewQueue(){
        return new Queue(reviewQueueName,false);
    }

    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("product.exchange");
    }

    @Bean
    public DirectExchange reviewExchange() {
        return new DirectExchange("review.exchange");
    }

    @Bean
    public Binding ReviewBinding() {
        return BindingBuilder
                .bind(reviewQueue())
                .to(reviewExchange())
                .with("review.event");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory());
    }

}
