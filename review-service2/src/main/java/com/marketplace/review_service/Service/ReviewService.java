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
import com.marketplace.review_service.Exception.*;
import com.marketplace.review_service.Mapper.ReviewEventMapper;
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


        if (reviewRepo.existsByUserIdAndProductId(userId, request.getProductId())) {
            throw new ReviewAlreadyExist("Review already exists");
        }

        boolean hasPurchased = orderClient.checkUserPurchase(userId, request.getProductId());
        if (!hasPurchased) {
            throw new ProductNotPurchasedException("User has not purchased this product");
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

        reviewEntity.setStatus(ReviewStatus.PENDING);
        reviewEntity.setVerifiedPurchase(hasPurchased);

        var saved = reviewRepo.save(reviewEntity);

        ReviewEvent event = createReviewEvent(saved, ReviewEventType.CREATED);

        rabbitProducer.sendReviewEvent(event);

        return ReviewMapper.toResponse(saved);
    }

    public ReviewResponse getReviewById(Long id) {
       return reviewRepo.findById(id)
                .map(ReviewMapper::toResponse)
                .orElseThrow(() -> new NotFoundById("Review not found by id: " + id));
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
            throw new InvalidRatingException("Rating must be between 1 and 5");
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

        var reviewEntity = reviewRepo.findById(id)
                .filter(re-> re.getUserId().equals(userId))
                .orElseThrow(() -> new NotFoundById("Review not found by id: " +id));

        if (request.getRating() != null) {
            reviewEntity.setRating(request.getRating());

            ReviewEvent event = createReviewEvent(reviewEntity, ReviewEventType.UPDATED);

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

        var reviewEntity = reviewRepo.findById(id)
                .filter(re-> re.getUserId().equals(userId))
                .orElseThrow(() -> new NotFoundById("Review not found by id: " +id));

        reviewRepo.deleteById(id);

        ReviewEvent event = createReviewEvent(reviewEntity, ReviewEventType.DELETED);

        rabbitProducer.sendReviewEvent(event);

    }

    public void markReviewAsHelpful(Long id, Long userId) {

        var reviewEntity = reviewRepo.findById(id)
                .orElseThrow(() -> new NotFoundById("Review not found"));

        boolean checkHelpful = reviewHelpfulRepo.existsByReviewIdAndUserId(id,userId);

        if (checkHelpful) {
            throw new AlreadyMarkedHelpfulException("User has already marked this review as helpful");
        }

        var helpfulVote = new ReviewHelpfulEntity();
        helpfulVote.setReview(reviewEntity);
        helpfulVote.setUserId(userId);
        reviewHelpfulRepo.save(helpfulVote);

        reviewEntity.incrementHelpfulCount();
        reviewRepo.save(reviewEntity);
    }

    public ReviewResponse approveReview(Long reviewId) {

        var reviewEntity = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new NotFoundById("Review not found by id: " + reviewId));

        if (reviewEntity.getStatus() != ReviewStatus.PENDING) {
            throw new ReviewCannotBeApprovedException("Only pending reviews can be approved");
        }

        reviewEntity.setModeratedAt(Instant.now());
        reviewEntity.setStatus(ReviewStatus.APPROVED);
        reviewRepo.save(reviewEntity);

        ReviewEvent event = createReviewEvent(reviewEntity, ReviewEventType.CREATED);

        rabbitProducer.sendReviewEvent(event);

        return ReviewMapper.toResponse(reviewEntity);
    }

    public ReviewResponse rejectReview(RejectReviewRequest request, Long reviewId) {

        var reviewEntity = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new NotFoundById("Review not found by id: " + reviewId));

        if (reviewEntity.getStatus() != ReviewStatus.PENDING) {
            throw new ReviewCannotBeRejectedException("Only pending reviews can be rejected");
        }

        reviewEntity.setModerationReason(request.getReason());
        reviewEntity.setModeratedBy(request.getModeratedBy());
        reviewEntity.setModeratedAt(Instant.now());

        reviewEntity.setStatus(ReviewStatus.REJECTED);
        reviewRepo.save(reviewEntity);

        return ReviewMapper.toResponse(reviewEntity);
    }

    private ReviewEvent createReviewEvent(ReviewEntity reviewEntity, ReviewEventType type) {
        ReviewEvent event = ReviewEventMapper.createReviewEvent(reviewEntity, type);
        double rating = reviewRepo.getAverageRating(reviewEntity.getProductId());
        Integer ratingCount = reviewRepo.countByProductIdAndStatus(reviewEntity.getProductId(), ReviewStatus.APPROVED);
        event.setRatingCount(ratingCount);
        event.setRating(rating);
        return event;
    }

    public void deleteReviewByAdmin(Long reviewId) {
        reviewRepo.deleteById(reviewId);
    }

    public Page<ReviewResponse> getPendingReviews(Pageable pageable) {
        return reviewRepo.findPendingReview(pageable)
                .map(ReviewMapper::toResponse);
    }
}
