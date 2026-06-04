package payment_service.payment_service.Request;

import jakarta.validation.constraints.NotBlank;

public class PaymentStatusRequest {
    @NotBlank(message = "paymentIntentId must not be empty")
    private String paymentIntentId;

    @NotBlank(message = "status must not be empty")
    private String status;

    public PaymentStatusRequest(String paymentIntentId, String status) {
        this.paymentIntentId = paymentIntentId;
        this.status = status;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
