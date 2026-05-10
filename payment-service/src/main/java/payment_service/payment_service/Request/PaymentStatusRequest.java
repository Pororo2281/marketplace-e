package payment_service.payment_service.Request;

public class PaymentStatusRequest {
    private String paymentIntentId;
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
