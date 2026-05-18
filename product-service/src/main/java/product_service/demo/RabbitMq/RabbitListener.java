package product_service.demo.RabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;
import product_service.demo.Entity.ProductEntity;
import product_service.demo.Event.ReviewEvent;
import product_service.demo.Repository.ProductRepo;

import java.math.BigDecimal;

@Service
public class RabbitListener {


    private final ObjectMapper objectMapper;
    private final ProductRepo productRepo;

    public RabbitListener(ObjectMapper objectMapper, ProductRepo productRepo) {
        this.objectMapper = objectMapper;
        this.productRepo = productRepo;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"${rabbitmq.queue.product}"})
    public void receiveMessage(String message) {
        try {
            ReviewEvent reviewEvent = objectMapper.readValue(message, ReviewEvent.class);
            if (reviewEvent==null) return;
            ProductEntity product = productRepo.findById(reviewEvent.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + reviewEvent.getProductId()));
            product.setRating(BigDecimal.valueOf(reviewEvent.getRating()));
            product.setReviewsCount(reviewEvent.getRatingCount());
            productRepo.save(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
