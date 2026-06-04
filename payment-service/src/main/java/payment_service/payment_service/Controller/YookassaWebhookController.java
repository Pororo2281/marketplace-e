package payment_service.payment_service.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payment_service.payment_service.Repository.PaymentRepo;
import payment_service.payment_service.Service.WebhookService;

import java.util.Map;

@RestController
@RequestMapping("api/webhook/yookassa")
public class YookassaWebhookController {

    private final WebhookService service;
    public YookassaWebhookController(WebhookService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> handleWebhook(@Valid @RequestBody Map<String, Object> payload) {
        System.out.println("webhook received: " + payload);
        service.handleWebhook(payload);
        return ResponseEntity.ok("Webhook processed");
    }
}
