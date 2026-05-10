package payment_service.payment_service.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_service.payment_service.Entity.PaymentEntity;
import payment_service.payment_service.Enum.PaymentStatus;
import payment_service.payment_service.RabbitMq.RabbitProducer;
import payment_service.payment_service.Repository.PaymentRepo;
import payment_service.payment_service.Response.PaymentSucceededEvent;

import java.util.Map;

@Service
public class WebhookService {

    private final PaymentRepo paymentRepo;
    private final RabbitProducer rabbitProducer;

    @Value("${internal.secret-token}")
    private String apiKey;

    public WebhookService(PaymentRepo paymentRepo, RabbitProducer rabbitService) {
        this.paymentRepo = paymentRepo;
        this.rabbitProducer = rabbitService;
    }

    @Transactional
    public void handleWebhook(Map<String, Object> payload) {

        String event = (String) payload.get("event");
        Map<String, Object> object = (Map<String, Object>) payload.get("object");
        String paymentId = (String) object.get("id");

        switch (event){
            case "payment.succeeded":
                updateOrderPaymentStatus("payment.succeeded",(paymentId));
                break;
            case "payment.canceled":
                updateOrderPaymentStatus("payment.canceled",(paymentId));
                break;
            case "payment.failed":
                updateOrderPaymentStatus("payment.failed",(paymentId));
                break;
        }
    }

    private void updateOrderPaymentStatus(String event,String paymentId) {
        String status = event.substring(8).toUpperCase();
        PaymentEntity payment = paymentRepo
                .findByYookassaPaymentId(paymentId)
                .orElseThrow();
        if (payment.getStatus() == PaymentStatus.valueOf(status)) return;

        payment.setStatus(PaymentStatus.valueOf(status));
        paymentRepo.save(payment);

        rabbitProducer.sendPaymentEvent(new PaymentSucceededEvent(
                    payment.getOrderId(),
                    payment.getYookassaPaymentId(), payment.getStatus().name(),
                apiKey
                ));
    }
}
