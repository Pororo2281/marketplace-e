package product_service.demo.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductImageRequest {

    @NotBlank(message = "Image URL cannot be empty")
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    @NotNull(message = "Sort order cannot be null")
    @Min(value = 0, message = "Sort order must be at least 0")
    private Integer sortOrder;
    public ProductImageRequest() {

    }

    public ProductImageRequest(String imageUrl, Integer sortOrder) {
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
