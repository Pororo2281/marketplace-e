package com.marketplace.review_service.Request;

import jakarta.validation.constraints.*;

import java.util.List;

public class CreateReviewRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long orderId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;

    private String pros;

    private String cons;

    // Если изображения загружаются отдельно — тут могут быть URL или ID
    private List<String> imageUrls;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
