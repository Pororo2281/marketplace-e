package com.marketplace.review_service.Mapper;

import com.marketplace.review_service.Entity.ReviewImageEntity;
import com.marketplace.review_service.dto.response.ReviewImageResponse;

public class ReviewImageMapper {

    public static ReviewImageResponse toResponse(ReviewImageEntity entity) {
        if (entity == null) return null;

        com.marketplace.review_service.dto.response.ReviewImageResponse response = new com.marketplace.review_service.dto.response.ReviewImageResponse();
        response.setId(entity.getId());
        response.setImageUrl(entity.getImageUrl());
        response.setThumbnailUrl(entity.getThumbnailUrl());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setUploadedAt(entity.getUploadedAt());

        return response;
    }
}
