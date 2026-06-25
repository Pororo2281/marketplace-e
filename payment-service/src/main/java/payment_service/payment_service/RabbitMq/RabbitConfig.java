package payment_service.payment_service.RabbitMq;

import com.rabbitmq.client.AMQP;
import jakarta.annotation.PostConstruct;
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
    public DirectExchange paymentExchange() {
        return new DirectExchange("payment.exchange");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory());
    }
}
