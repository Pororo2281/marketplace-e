package com.marketplace.review_service.Specifications;


import com.marketplace.review_service.Entity.ReviewEntity;
import com.marketplace.review_service.Enum.ReviewStatus;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecifications {

    public static Specification<ReviewEntity> hasProductId(Long productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("productId"), productId);
        };
    }

    public static Specification<ReviewEntity> hasRating(Integer rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("rating"), rating);
        };
    }

        public static Specification<ReviewEntity> hasUserId(Long userId) {
            return (root, query, criteriaBuilder) -> {
                if (userId == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("userId"), userId);
            };
        }

    public static Specification<ReviewEntity> isApproved() {
        return (root, query, cb) ->
                cb.equal(root.get("status"), ReviewStatus.APPROVED);
    }

    public static Specification<ReviewEntity> hasSellerId(Long sellerId) {
        return (root, query, cb) -> {
            if (sellerId == null) return cb.conjunction();
            return cb.equal(root.get("sellerId"), sellerId);
        };
    }

    public static Specification<ReviewEntity> hasMinRating(Integer minRating) {
        return (root, query, cb) -> {
            if (minRating == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
        };
    }

    public static Specification<ReviewEntity> hasImages(Boolean withImages) {
        return (root, query, cb) -> {
            if (withImages == null || !withImages) {
                return cb.conjunction();
            }
            return cb.isNotEmpty(root.get("images"));
        };
    }

    public static Specification<ReviewEntity> isVerifiedPurchase() {
        return (root, query, cb) ->
                cb.isTrue(root.get("verifiedPurchase"));
    }
}
