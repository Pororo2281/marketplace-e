package product_service.demo.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateStockRequest {

    @NotNull(message = "Stock quantity cannot be null")
    @Min(value = 0, message = "Stock quantity must be at least 0")
    private Integer stockQuantity;

    public UpdateStockRequest() {
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
