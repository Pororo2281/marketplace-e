package order_service.order.Entity;

import jakarta.persistence.*;
import order_service.order.Enum.OrderStatus;
import order_service.order.Enum.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_order_id", nullable = false)
    private MainOrderEntity mainOrder;


    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal tax;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total; // subtotal + tax


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(name = "payment_intent_id")
    private String paymentIntentId; // Stripe Payment Intent ID

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "buyer_email", nullable = false)
    private String buyerEmail;

    // Даты
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();

        if (orderNumber == null) {
            orderNumber = "ORD-" + Instant.now().toEpochMilli();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MainOrderEntity getMainOrder() {
        return mainOrder;
    }

    public void setMainOrder(MainOrderEntity mainOrder) {
        this.mainOrder = mainOrder;
    }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<OrderItemEntity> getItems() { return items; }
    public void setItems(List<OrderItemEntity> items) { this.items = items; }

    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }

    public Instant getPaidAt() { return paidAt; }
    public void setPaidAt(Instant paidAt) { this.paidAt = paidAt; }

    public Instant getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(Instant cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public Instant getRefundedAt() { return refundedAt; }
    public void setRefundedAt(Instant refundedAt) { this.refundedAt = refundedAt; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}