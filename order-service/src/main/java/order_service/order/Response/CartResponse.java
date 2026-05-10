package order_service.order.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartResponse {

    private Long userId;
    private List<CartItemResponse> items = new ArrayList<>();
    private Integer totalItems; // Общее количество товаров
    private BigDecimal subtotal; // Общая сумма
    private BigDecimal tax;
    private BigDecimal total;

    public CartResponse() {}

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}