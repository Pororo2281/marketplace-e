package product_service.demo.Event;

import product_service.demo.Enum.ReviewEventType;

public class ReviewEvent {

    private Long reviewId;
    private Long productId;
    private Double rating;
    private Integer ratingCount;
    private ReviewEventType type;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ReviewEventType getType() {
        return type;
    }

    public void setType(ReviewEventType type) {
        this.type = type;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

}
