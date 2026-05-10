package com.marketplace.review_service.Service;
import com.marketplace.review_service.Client.OrderClient;
import com.marketplace.review_service.Client.ProductClient;
import com.marketplace.review_service.Client.UserClient;
import com.marketplace.review_service.Entity.ReviewEntity;
import com.marketplace.review_service.Entity.ReviewHelpfulEntity;
import com.marketplace.review_service.Entity.ReviewImageEntity;
import com.marketplace.review_service.Enum.ReviewEventType;
import com.marketplace.review_service.Enum.ReviewSortBy;
import com.marketplace.review_service.Enum.ReviewStatus;
import com.marketplace.review_service.Enum.SortOrder;
import com.marketplace.review_service.Event.ReviewEvent;
import com.marketplace.review_service.Mapper.ReviewMapper;
import com.marketplace.review_service.RabbitMq.RabbitProducer;
import com.marketplace.review_service.Repository.ReviewHelpfulRepo;
import com.marketplace.review_service.Repository.ReviewRepo;
import com.marketplace.review_service.Request.CreateReviewRequest;
import com.marketplace.review_service.Request.UpdateReviewRequest;
import com.marketplace.review_service.Request.RejectReviewRequest;
import com.marketplace.review_service.Response.ReviewResponse;
import com.marketplace.review_service.Specifications.ReviewSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final RabbitProducer rabbitProducer;
    private final ReviewHelpfulRepo reviewHelpfulRepo;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public ReviewService(ReviewRepo reviewRepo, OrderClient orderClient, UserClient userClient, ProductClient productClient, RabbitProducer rabbitProducer, ReviewHelpfulRepo reviewHelpfulRepo) {
        this.reviewRepo = reviewRepo;
        this.orderClient = orderClient;
        this.userClient = userClient;
        this.productClient = productClient;
        this.rabbitProducer = rabbitProducer;
        this.reviewHelpfulRepo = reviewHelpfulRepo;
    }

    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {

        log.info("Creating review: userId={}, productId={}", userId, request.getProductId());

        if (reviewRepo.existsByUserIdAndProductId(userId, request.getProductId())) {
            throw new IllegalStateException("Review already exists");
        }

        boolean hasPurchased = orderClient.checkUserPurchase(userId, request.getProductId());
        if (!hasPurchased) {
            throw new IllegalStateException("User has not purchased this product");
        }

        var user = userClient.getUserById(userId);
        var productResponse = productClient.getSellerIdByProductId(request.getProductId());

        var reviewEntity = new ReviewEntity();

        reviewEntity.setUserId(userId);
        reviewEntity.setProductId(request.getProductId());
        reviewEntity.setOrderId(request.getOrderId());

        reviewEntity.setRating(request.getRating());
        reviewEntity.setTitle(request.getTitle());
        reviewEntity.setContent(request.getContent());
        reviewEntity.setPros(request.getPros());
        reviewEntity.setCons(request.getCons());

        reviewEntity.setUserName(user.getFirstName() + " " + user.getLastName());
        reviewEntity.setSellerId(productResponse.getSellerId());
        reviewEntity.setProductTitle(productResponse.getTitle());

        if (request.getImageUrls() != null) {
            request.getImageUrls().forEach(url -> {
                ReviewImageEntity image = new ReviewImageEntity();
                image.setImageUrl(url);
                reviewEntity.addImage(image);
            });
        }

        reviewEntity.setStatus(ReviewStatus.APPROVED);//PENDING
        reviewEntity.setVerifiedPurchase(hasPurchased);

        var saved = reviewRepo.save(reviewEntity);
        double raiting = reviewRepo.getAverageRating(request.getProductId());
        Integer ratingCount = reviewRepo.countByProductIdAndStatus(request.getProductId(), ReviewStatus.APPROVED);

        ReviewEvent event = new ReviewEvent();
        event.setReviewId(reviewEntity.getId());
        event.setRating(raiting);
        event.setProductId(reviewEntity.getProductId());
        event.setType(ReviewEventType.CREATED);
        event.setRatingCount(ratingCount);

        rabbitProducer.sendReviewEvent(event);

        return ReviewMapper.toResponse(saved);
    }

    public ReviewResponse getReviewById(Long id) {
       return reviewRepo.findById(id)
                .map(ReviewMapper::toResponse)
                .orElseThrow(() -> new IllegalStateException("Review not found"));
    }

    public Page<ReviewResponse> getReviewsByProductId(
            Long productId,
            Integer page,
            Integer limit,
            Integer rating,
            Long sellerId,
            Long userId,
            Boolean withImages,
            ReviewSortBy sortBy,
            SortOrder order
    ) {
        Sort sort = Sort.by(
                Sort.Direction.valueOf(order.name()),
                sortBy.name()
        );

        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Pageable pageable = PageRequest.of(
                page,
                limit,
                sort
        );

        Specification<ReviewEntity> specification = ReviewSpecifications.hasMinRating(rating)
                .and(ReviewSpecifications.hasSellerId(sellerId))
                .and(ReviewSpecifications.hasUserId(userId))
                .and(ReviewSpecifications.isApproved())
                .and(ReviewSpecifications.hasProductId(productId))
                .and(ReviewSpecifications.isVerifiedPurchase())
                .and(ReviewSpecifications.hasImages(withImages));


        var reviewsPage = reviewRepo.findAll(specification,pageable);

        return reviewsPage.map(ReviewMapper::toResponse);
    }

    @Transactional
    public ReviewResponse updateReview(Long id,  UpdateReviewRequest request,Long userId) {

        log.info("Updating review: reviewId={}, userId={}", id, userId);

        var reviewEntity = reviewRepo.findById(id)
                .filter(re-> re.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalStateException("Review not found"));

        if (request.getRating() != null) {
            reviewEntity.setRating(request.getRating());

            double raiting = reviewRepo.getAverageRating(reviewEntity.getProductId());
            Integer ratingCount = reviewRepo.countByProductIdAndStatus(reviewEntity.getProductId(), ReviewStatus.APPROVED);

            ReviewEvent event = new ReviewEvent();
            event.setReviewId(reviewEntity.getId());
            event.setRating(raiting);
            event.setProductId(reviewEntity.getProductId());
            event.setType(ReviewEventType.UPDATED);
            event.setRatingCount(ratingCount);

            rabbitProducer.sendReviewEvent(event);
        }

        if (request.getTitle() != null) {
            reviewEntity.setTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            reviewEntity.setContent(request.getContent());
        }

        if (request.getPros() != null) {
            reviewEntity.setPros(request.getPros());
        }

        if (request.getCons() != null) {
            reviewEntity.setCons(request.getCons());
        }

        var updated = reviewRepo.save(reviewEntity);

        return ReviewMapper.toResponse(updated);
    }

    @Transactional
    public void deleteReview(Long id,Long userId) {

        log.info("Deleting review: reviewId={}, userId={}", id, userId);

        var reviewEntity = reviewRepo.findById(id)
                .filter(re-> re.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalStateException("Review not found"));

        reviewRepo.deleteById(id);

        double raiting = reviewRepo.getAverageRating(reviewEntity.getProductId());
        Integer ratingCount = reviewRepo.countByProductIdAndStatus(reviewEntity.getProductId(), ReviewStatus.APPROVED);

        ReviewEvent event = new ReviewEvent();
        event.setReviewId(reviewEntity.getId());
        event.setRating(raiting);
        event.setProductId(reviewEntity.getProductId());
        event.setType(ReviewEventType.DELETED);
        event.setRatingCount(ratingCount);

        rabbitProducer.sendReviewEvent(event);

    }

    public void markReviewAsHelpful(Long id, Long userId) {

        log.info("Marking review as helpful: reviewId={}, userId={}", id, userId);

        var reviewEntity = reviewRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Review not found"));

        boolean checkHelpful = reviewHelpfulRepo.existsByReviewIdAndUserId(id,userId);

        if (checkHelpful) {
            throw new IllegalStateException("User has already marked this review as helpful");
        }

        var helpfulVote = new ReviewHelpfulEntity();
        helpfulVote.setReview(reviewEntity);
        helpfulVote.setUserId(userId);
        reviewHelpfulRepo.save(helpfulVote);

        reviewEntity.incrementHelpfulCount();
        reviewRepo.save(reviewEntity);
    }

    public ReviewResponse approveReview(Long reviewId) {
        log.info("Approving review: reviewId={}", reviewId);

        var reviewEntity = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found"));

        if (reviewEntity.getStatus() != ReviewStatus.PENDING) {
            throw new IllegalStateException("Only pending reviews can be approved");
        }

        reviewEntity.setModeratedAt(Instant.now());
        reviewEntity.setStatus(ReviewStatus.APPROVED);
        reviewRepo.save(reviewEntity);

        double rating = reviewRepo.getAverageRating(reviewEntity.getProductId());
        Integer ratingCount = reviewRepo.countByProductIdAndStatus(reviewEntity.getProductId(), ReviewStatus.APPROVED);

        ReviewEvent event = new ReviewEvent();
        event.setReviewId(reviewEntity.getId());
        event.setRating(rating);
        event.setProductId(reviewEntity.getProductId());
        event.setType(ReviewEventType.CREATED);
        event.setRatingCount(ratingCount);

        rabbitProducer.sendReviewEvent(event);

        return ReviewMapper.toResponse(reviewEntity);
    }

    public ReviewResponse rejectReview(RejectReviewRequest request, Long reviewId) {
        log.info("Rejecting review: reviewId={}", reviewId);

        var reviewEntity = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found"));

        if (reviewEntity.getStatus() != ReviewStatus.PENDING) {
            throw new IllegalStateException("Only pending reviews can be rejected");
        }

        reviewEntity.setModerationReason(request.getReason());
        reviewEntity.setModeratedBy(request.getModeratedBy());
        reviewEntity.setModeratedAt(Instant.now());

        reviewEntity.setStatus(ReviewStatus.REJECTED);
        reviewRepo.save(reviewEntity);

        return ReviewMapper.toResponse(reviewEntity);
    }
}
