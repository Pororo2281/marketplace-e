package com.marketplace.review_service.Mapper;

import com.marketplace.review_service.Entity.ReviewEntity;
import com.marketplace.review_service.Enum.ReviewEventType;
import com.marketplace.review_service.Event.ReviewEvent;

public class ReviewEventMapper {
    public static ReviewEvent createReviewEvent(ReviewEntity entity , ReviewEventType type){
        ReviewEvent event = new ReviewEvent();
        event.setReviewId(entity.getId());
        event.setProductId(entity.getProductId());
        event.setType(type);
        return event;
    }
}
