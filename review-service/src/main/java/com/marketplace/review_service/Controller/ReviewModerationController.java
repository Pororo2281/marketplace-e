package com.marketplace.review_service.Controller;

import com.marketplace.review_service.Request.RejectReviewRequest;
import com.marketplace.review_service.Response.ReviewResponse;
import com.marketplace.review_service.Service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
