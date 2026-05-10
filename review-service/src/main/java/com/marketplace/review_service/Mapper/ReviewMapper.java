package com.marketplace.review_service.Mapper;

import com.marketplace.review_service.Entity.ReviewEntity;
import com.marketplace.review_service.Entity.ReviewImageEntity;
import com.marketplace.review_service.Response.ReviewResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {
    public static ReviewResponse toResponse(ReviewEntity entity) {
        if (entity == null) return null;

        ReviewResponse response = new ReviewResponse();

        response.setId(entity.getId());

        response.setUserId(entity.getUserId());
        response.setUserName(entity.getUserName());
        response.setUserAvatarUrl(entity.getUserAvatarUrl());

        response.setProductId(entity.getProductId());
        response.setProductTitle(entity.getProductTitle());

        response.setRating(entity.getRating());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setPros(entity.getPros());
        response.setCons(entity.getCons());

        response.setHelpfulCount(entity.getHelpfulCount());
        response.setVerifiedPurchase(entity.getVerifiedPurchase());
        response.setStatus(entity.getStatus());

        response.setCreatedAt(entity.getCreatedAt());

        if (entity.getImages() != null) {
            List<String> imageUrls = entity.getImages()
                    .stream()
                    .map(ReviewImageEntity::getImageUrl)
                    .collect(Collectors.toList());

            response.setImageUrls(imageUrls);
        }

        return response;
    }
}
