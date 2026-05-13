package com.marketplace.review_service.Repository;
import com.marketplace.review_service.Entity.ReviewHelpfulEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewHelpfulRepo extends JpaRepository<ReviewHelpfulEntity, Long> {

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    Optional<ReviewHelpfulEntity> findByReviewIdAndUserId(Long reviewId, Long userId);

    void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}