package payment_service.payment_service.Entity;

import jakarta.persistence.*;
import payment_service.payment_service.Enum.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "refunds")
public class RefundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;


    @Column(name = "yookassa_refund_id", nullable = false, unique = true)
    private String yookassaRefundId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency = "RUB";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public RefundEntity() {
    }

    // ===== Getters и Setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public String getYookassaRefundId() {
        return yookassaRefundId;
    }

    public void setYookassaRefundId(String yookassaRefundId) {
        this.yookassaRefundId = yookassaRefundId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}