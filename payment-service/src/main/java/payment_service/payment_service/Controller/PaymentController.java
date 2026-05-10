package payment_service.payment_service.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment_service.payment_service.Request.CreatePaymentRequest;
import payment_service.payment_service.Response.PaymentResponse;
import payment_service.payment_service.Service.YookassaService;

@RestController
@RequestMapping("api/payments")
public class PaymentController {

    private final YookassaService service;
    public PaymentController(YookassaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestHeader("X-USER-ID") Long userId,
                                                         @Valid @RequestBody CreatePaymentRequest request){
        return ResponseEntity.ok(service.createPayment(request,userId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getStatus(@RequestHeader("X-USER-ID") Long userId,@PathVariable Long paymentId){
        return ResponseEntity.ok(service.getStatus(paymentId,userId));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@RequestHeader("X-USER-ID") Long userId,@PathVariable Long paymentId){
        return ResponseEntity.ok(service.refundPayment(paymentId,userId));
    }

}
