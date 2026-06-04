package com.marketplace.review_service.Controller;

import com.marketplace.review_service.Request.RejectReviewRequest;
import com.marketplace.review_service.Response.ReviewResponse;
import com.marketplace.review_service.Service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews/admin")
public class ReviewModerationController {

    private final ReviewService reviewService;

    public ReviewModerationController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PatchMapping("/{reviewId}/approve")
    public ResponseEntity<ReviewResponse> approveReview(Long reviewId) {
        return ResponseEntity.ok(reviewService.approveReview(reviewId));
    }

    @PatchMapping("/{reviewId}/reject")
    public ResponseEntity<ReviewResponse> rejectReview(@Valid @RequestBody RejectReviewRequest request, Long reviewId) {
        return ResponseEntity.ok(reviewService.rejectReview(request,reviewId));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getPendingReviews(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getPendingReviews(pageable));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReviewByAdmin(reviewId);
        return ResponseEntity.noContent().build();
    }
}
