package com.marketplace.review_service.dto.response;

import com.marketplace.review_service.Entity.ReviewImageEntity;

import java.time.Instant;

public class ReviewImageResponse {

    private Long id;
    private String imageUrl;
    private String thumbnailUrl;
    private Integer displayOrder;
    private Instant uploadedAt;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

}