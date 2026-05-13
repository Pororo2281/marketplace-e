package com.marketplace.review_service.Request;

import com.marketplace.review_service.Enum.ReviewSortBy;
import com.marketplace.review_service.Enum.ReviewStatus;
import com.marketplace.review_service.Enum.SortOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class GetReviewsQuery {

    @Min(value = 0, message = "page must be >= 0")
    private Integer page = 0;

    @Min(value = 1, message = "limit must be >= 1")
    @Max(value = 100, message = "limit must be <= 100")
    private Integer limit = 20;

    private Long productId;

    private Long userId;

    private Long sellerId;

    private ReviewStatus status;

    @Min(value = 1) @Max(value = 5)
    private Integer rating;

    private Boolean verifiedPurchase;

    private Boolean withImages;

    private ReviewSortBy sortBy = ReviewSortBy.createdAt;

    private SortOrder order = SortOrder.desc;

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Boolean getWithImages() {
        return withImages;
    }

    public ReviewSortBy getSortBy() {
        return sortBy;
    }

    public SortOrder getOrder() {
        return order;
    }

    public void setSortBy(ReviewSortBy sortBy) {
        this.sortBy = sortBy;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }

    public void setWithImages(Boolean withImages) {
        this.withImages = withImages;
    }

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Boolean getVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(Boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

}