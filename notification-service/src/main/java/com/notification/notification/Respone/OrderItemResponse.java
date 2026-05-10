package com.notification.notification.Respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemResponse {

    private Integer quantity;
    private String productTitle;
    private BigDecimal subtotal;

    public OrderItemResponse() {

    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
}
