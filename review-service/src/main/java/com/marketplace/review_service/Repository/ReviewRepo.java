package com.marketplace.review_service.Repository;

import com.marketplace.review_service.Entity.ReviewEntity;
import com.marketplace.review_service.Enum.ReviewStatus;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity, Long>, JpaSpecificationExecutor<ReviewEntity> {


    Page<ReviewEntity> findAll(
            Specification<ReviewEntity> spec,
            Pageable pageable
    );


    Page<ReviewEntity> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );


    Optional<ReviewEntity> findByUserIdAndProductId(Long userId, Long productId);


    boolean existsByUserIdAndProductId(Long userId, Long productId);


    Page<ReviewEntity> findBySellerIdAndStatus(
            Long sellerId,
            ReviewStatus status,
            Pageable pageable
    );


    Page<ReviewEntity> findByStatusOrderByCreatedAtAsc(
            ReviewStatus status,
            Pageable pageable
    );


    @Query("SELECT AVG(r.rating) FROM ReviewEntity r " +
            "WHERE r.productId = :productId AND r.status = 'APPROVED'")
    Double getAverageRating(@Param("productId") Long productId);


    Integer countByProductIdAndStatus(Long productId, ReviewStatus status);


    @Query("SELECT r.rating, COUNT(r) FROM ReviewEntity r " +
            "WHERE r.productId = :productId AND r.status = 'APPROVED' " +
            "GROUP BY r.rating")
    Object[][] getRatingDistribution(@Param("productId") Long productId);


    List<ReviewEntity> findByProductIdAndStatus(Long productId, ReviewStatus status);
}