package payment_service.payment_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import payment_service.payment_service.Request.PaymentStatusRequest;

@FeignClient(name = "order-service",url = "${services.order-service}")
public interface OrderClient {

    @PostMapping("/api/internal/orders/{orderId}/payment-status")
    void updatePaymentStatus(@PathVariable Long orderId, @RequestBody PaymentStatusRequest request);

}
