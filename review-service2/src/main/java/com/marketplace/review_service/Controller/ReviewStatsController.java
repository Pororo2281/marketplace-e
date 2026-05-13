package com.marketplace.review_service.Controller;

import com.marketplace.review_service.Response.ReviewStatsResponse;
import com.marketplace.review_service.Service.ReviewStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reviews/stats")
public class ReviewStatsController {

    private final ReviewStatsService reviewStatsService;
    private static final Logger log = LoggerFactory.getLogger(ReviewStatsController.class);

    public ReviewStatsController(ReviewStatsService reviewStatsService) {
        this.reviewStatsService = reviewStatsService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ReviewStatsResponse> getReviewStatsForProduct(@PathVariable Long productId) {
        log.info("Fetching review stats for productId={}", productId);
        return ResponseEntity.ok(reviewStatsService.getReviewStatsForProduct(productId));
    }

}
