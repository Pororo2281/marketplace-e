package order_service.order.Response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderMailResponse {

    private String email;
    private String orderNumber;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Instant paidAt;
    private List<OrderItemResponse> orderItems;


    public OrderMailResponse() {
    }

    public OrderMailResponse(String email, String orderNumber, BigDecimal totalPrice, Instant createdAt, Instant paidAt, List<OrderItemResponse> orderItems) {
        this.email = email;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.orderItems = orderItems;
    }

    public OrderMailResponse(List<OrderItemResponse> orderItems, Instant createdAt, BigDecimal totalPrice, String orderNumber, String email) {
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.orderNumber = orderNumber;
        this.email = email;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
}
