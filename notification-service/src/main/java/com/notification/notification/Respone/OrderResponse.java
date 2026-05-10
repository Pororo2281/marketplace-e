package com.notification.notification.Respone;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponse {

    private String email;
    private String orderNumber;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Instant paidAt;
    private List<OrderItemResponse> orderItems;

    public OrderResponse() {
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
