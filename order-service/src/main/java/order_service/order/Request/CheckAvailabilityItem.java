package order_service.order.Request;

import jakarta.validation.constraints.NotNull;

public class CheckAvailabilityItem {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;


    public CheckAvailabilityItem() {}

    public CheckAvailabilityItem(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "CheckAvailabilityItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
