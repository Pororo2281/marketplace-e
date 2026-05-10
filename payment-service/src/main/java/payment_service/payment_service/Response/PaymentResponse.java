package payment_service.payment_service.Response;
import java.math.BigDecimal;

public class PaymentResponse {

    private String paymentId;
    private String confirmationUrl;
    private BigDecimal amount;
    private String status;

    public PaymentResponse() {}

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getConfirmationUrl() { return confirmationUrl; }
    public void setConfirmationUrl(String confirmationUrl) { this.confirmationUrl = confirmationUrl; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
