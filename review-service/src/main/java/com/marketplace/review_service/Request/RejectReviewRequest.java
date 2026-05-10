package com.marketplace.review_service.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RejectReviewRequest {

        @NotNull(message = "moderatedBy is required")
        private Long moderatedBy;

        @NotBlank(message = "reason is required")
        private String reason;

        public Long getModeratedBy() { return moderatedBy; }
        public void setModeratedBy(Long moderatedBy) { this.moderatedBy = moderatedBy; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

}
