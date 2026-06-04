package search.search.Rabbit;

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

    @Value("${rabbitmq.queue.product}")
    private  String queueName;

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public CachingConnectionFactory connectionFactory(){
        String host = System.getenv().getOrDefault("SPRING_RABBITMQ_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("SPRING_RABBITMQ_PORT", "5672"));
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue searchQueue() {
        return new Queue(queueName,false);
    }

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange("product.exchange");
    }

    @Bean
    public Binding productCreatedBinding() {
        return BindingBuilder
                .bind(searchQueue())
                .to(productExchange())
                .with("product.created");
    }

    @Bean
    public Binding productUpdatedBinding() {
        return BindingBuilder
                .bind(searchQueue())
                .to(productExchange())
                .with("product.updated");
    }

    @Bean
    public Binding productDeletedBinding() {
        return BindingBuilder
                .bind(searchQueue())
                .to(productExchange())
                .with("product.deleted");
    }

}

