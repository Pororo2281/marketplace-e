package order_service.order.Request;

import jakarta.validation.constraints.NotNull;
import order_service.order.Enum.OrderStatus;

public class UpdateOrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;

    private String reason;

    public UpdateOrderStatusRequest() {}

    public UpdateOrderStatusRequest(OrderStatus status) {
        this.status = status;
    }

    public UpdateOrderStatusRequest(OrderStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    // Getters and Setters
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}