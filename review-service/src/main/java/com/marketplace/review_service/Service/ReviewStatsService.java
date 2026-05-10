package com.marketplace.review_service.Service;

import com.marketplace.review_service.Enum.ReviewStatus;
import com.marketplace.review_service.Repository.ReviewRepo;
import com.marketplace.review_service.Response.ReviewStatsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReviewStatsService {

    private final ReviewRepo reviewRepo;
    private static final Logger log = LoggerFactory.getLogger(ReviewStatsService.class);

    public ReviewStatsService(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }


    public ReviewStatsResponse getReviewStatsForProduct(Long productId) {

        log.info("Fetching review stats for productId={}", productId);

        long approvedReviews = reviewRepo.countByProductIdAndStatus(productId, ReviewStatus.APPROVED);
        long totalReviews = reviewRepo.countByProductIdAndStatus(productId, ReviewStatus.PENDING) +
                approvedReviews +
                reviewRepo.countByProductIdAndStatus(productId, ReviewStatus.REJECTED);
        var averageRating = reviewRepo.getAverageRating(productId);
        averageRating = averageRating == null ? 0.0 : averageRating;

        var reviews = reviewRepo.findByProductIdAndStatus(productId, ReviewStatus.APPROVED);

        long rating1 = reviews.stream().
                filter(r -> r.getRating() == 1.0).toList().size();
        long rating2 = reviews.stream().
                filter(r -> r.getRating() == 2.0).toList().size();
        long rating3 = reviews.stream().
                filter(r -> r.getRating() == 3.0).toList().size();
        long rating4 = reviews.stream().
                filter(r -> r.getRating() == 4.0).toList().size();
        long rating5 = reviews.stream().
                filter(r -> r.getRating() == 5.0).toList().size();

        Map<Integer,Long> ratingDistribution = Map.of(
                1,  rating1,
                2,  rating2,
                3,  rating3,
                4,  rating4,
                5,  rating5
        );

        ReviewStatsResponse response = new ReviewStatsResponse();
        response.setProductId(productId);
        response.setAverageRating(averageRating);
        response.setTotalReviews(totalReviews);
        response.setApprovedReviews(approvedReviews);
        response.setRatingDistribution(ratingDistribution);
        return response;
    }
}
