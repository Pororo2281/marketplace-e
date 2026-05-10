package com.marketplace.review_service.Repository;

import com.marketplace.review_service.Entity.SellerResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerResponseRepo extends JpaRepository<SellerResponseEntity, Long> {

    Optional<SellerResponseEntity> findByReviewId(Long reviewId);

    boolean existsByReviewId(Long reviewId);
}
