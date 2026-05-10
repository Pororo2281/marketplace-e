package payment_service.payment_service.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreatePaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long mainOrderId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1 RUB")
    private BigDecimal amount;

    private String description;

    public CreatePaymentRequest() {}

    public Long getMainOrderId() { return mainOrderId; }
    public void setMainOrderId(Long mainOrderId) { this.mainOrderId = mainOrderId; }



    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}