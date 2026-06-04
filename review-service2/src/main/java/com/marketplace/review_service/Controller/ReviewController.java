package com.marketplace.review_service.Controller;

import com.marketplace.review_service.Enum.ReviewSortBy;
import com.marketplace.review_service.Enum.SortOrder;
import com.marketplace.review_service.Request.CreateReviewRequest;
import com.marketplace.review_service.Request.UpdateReviewRequest;
import com.marketplace.review_service.Response.ReviewResponse;
import com.marketplace.review_service.Service.ReviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<?> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        log.info("Create review request: userId={}, productId={}", userId, request.getProductId());
        return ResponseEntity.ok(
                reviewService.createReview(userId, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        log.debug("Fetching review by id={}", id);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "20", required = false) Integer limit,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Boolean withImages,
            @RequestParam(defaultValue = "createdAt") ReviewSortBy sortBy,
            @RequestParam(defaultValue = "desc") SortOrder order
    ) {
        return ResponseEntity.ok(
                reviewService.getReviewsByProductId(
                        productId,
                        page,
                        limit,
                        rating,
                        sellerId,
                        userId,
                        withImages,
                        sortBy,
                        order
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request,
            @RequestHeader("X-USER-ID") Long userId){
        log.info("Update review request: reviewId={}, userId={}", id, userId);
        return ResponseEntity.ok(reviewService.updateReview(id, request,userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id,
                                             @RequestHeader("X-USER-ID") Long userId) {
        reviewService.deleteReview(id,userId);
        log.info("Deleted review: reviewId={}, userId={}", id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/helpful")
    public ResponseEntity<Void> markReviewAsHelpful(@PathVariable Long id,
                                                    @RequestHeader("X-USER-ID") Long userId) {
        log.info("Mark review as helpful: reviewId={}, userId={}", id, userId);
        reviewService.markReviewAsHelpful(id, userId);
        return ResponseEntity.noContent().build();
    }
}
