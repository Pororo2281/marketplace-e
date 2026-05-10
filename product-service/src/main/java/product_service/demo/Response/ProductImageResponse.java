package product_service.demo.Response;

public class ProductImageResponse {
    private Long id;

    private String imageUrl;

    private Integer sortOrder;

    public ProductImageResponse() {
    }

    public ProductImageResponse(Long id, String imageUrl, Integer sortOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
