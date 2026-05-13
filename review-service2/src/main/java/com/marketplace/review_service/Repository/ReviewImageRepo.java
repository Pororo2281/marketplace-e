package com.marketplace.review_service.Repository;

import com.marketplace.review_service.Entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepo extends JpaRepository<ReviewImageEntity, Long> {

    List<ReviewImageEntity> findByReviewIdOrderByDisplayOrderAsc(Long reviewId);

    List<ReviewImageEntity> findByReviewIdOrderByDisplayOrder(Long id);

}