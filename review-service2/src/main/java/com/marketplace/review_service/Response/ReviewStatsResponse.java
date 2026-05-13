package com.marketplace.review_service.Response;

import java.util.Map;

public class ReviewStatsResponse {
    private Long productId;
    private Double averageRating;
    private Long totalReviews;
    private Long approvedReviews;

    private Map<Integer, Long> ratingDistribution;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Long totalReviews) { this.totalReviews = totalReviews; }

    public Long getApprovedReviews() { return approvedReviews; }
    public void setApprovedReviews(Long approvedReviews) { this.approvedReviews = approvedReviews; }

    public Map<Integer, Long> getRatingDistribution() { return ratingDistribution; }
    public void setRatingDistribution(Map<Integer, Long> ratingDistribution) { this.ratingDistribution = ratingDistribution; }
}
