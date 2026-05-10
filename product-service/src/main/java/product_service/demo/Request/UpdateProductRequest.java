package product_service.demo.Request;

import jakarta.validation.constraints.*;
import product_service.demo.Enum.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public class UpdateProductRequest {

    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Old price must be greater than 0")
    private BigDecimal oldPrice;

    @Min(value = 0, message = "Stock quantity must be at least 0")
    private Integer stockQuantity;

    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    private ProductStatus status;

    private List<UpdateAttributeRequest> attributes;

    public UpdateProductRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getOldPrice() { return oldPrice; }
    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public List<UpdateAttributeRequest> getAttributes() { return attributes; }
    public void setAttributes(List<UpdateAttributeRequest> attributes) { this.attributes = attributes; }
}