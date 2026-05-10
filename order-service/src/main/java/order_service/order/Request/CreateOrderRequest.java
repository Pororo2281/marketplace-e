package order_service.order.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {

    @NotEmpty(message = "Cart items cannot be empty")
    private List<Long> cartItemIds;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String buyerEmail;

    private List<CreateOrderItemRequest> items;

    public CreateOrderRequest() {}

    // Getters and Setters
    public List<Long> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Long> cartItemIds) { this.cartItemIds = cartItemIds; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public List<CreateOrderItemRequest> getItems() { return items; }
    public void setItems(List<CreateOrderItemRequest> items) { this.items = items; }


    public static class CreateOrderItemRequest {
        private Long productId;
        private Integer quantity;

        public CreateOrderItemRequest() {}

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}