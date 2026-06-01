package com.notification.notification.Event;

import com.notification.notification.Enum.NotificationEventType;
import com.notification.notification.Enum.OrderEventType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderEvent implements NotificationEvent {

    private String email;
    private String orderNumber;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Instant paidAt;
    private List<OrderItemEvent> orderItems;
    private OrderEventType orderEventType;

    public OrderEvent() {
    }

    public OrderEventType getOrderEventType() {
        return orderEventType;
    }

    public void setOrderEventType(OrderEventType orderEventType) {
        this.orderEventType = orderEventType;
    }

    public List<OrderItemEvent> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEvent> orderItems) {
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

    @Override
    public NotificationEventType getType() {
        return NotificationEventType.valueOf(orderEventType.name());
    }
}
