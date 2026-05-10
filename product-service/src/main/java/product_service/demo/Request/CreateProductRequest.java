package product_service.demo.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import product_service.demo.Enum.ProductStatus;
import product_service.demo.Request.ProductAttributeRequest;

import java.math.BigDecimal;
import java.util.List;

public class CreateProductRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity cannot be null")
    @Min(value = 0, message = "Stock quantity must be at least 0")
    private Integer stockQuantity;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @Valid
    @NotNull(message = "Attributes list cannot be null")
    private List<ProductAttributeRequest> attributes;

    // Конструкторы
    public CreateProductRequest() {}

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<ProductAttributeRequest> getAttributes() { return attributes; }
    public void setAttributes(List<ProductAttributeRequest> attributes) { this.attributes = attributes; }
}