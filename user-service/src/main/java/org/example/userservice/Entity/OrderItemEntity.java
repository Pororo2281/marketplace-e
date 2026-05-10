package org.example.userservice.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity entity;

    @Column(name = "price_at_purchase", nullable = false)
    private BigDecimal priceAtPurchase;

    public OrderItemEntity() {
    }

    public OrderItemEntity(OrderEntity order, Integer quantity, ProductEntity entity, BigDecimal priceAtPurchase) {
        this.order = order;
        this.quantity = quantity;
        this.entity = entity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductEntity getEntity() {
        return entity;
    }

    public void setEntity(ProductEntity entity) {
        this.entity = entity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}
