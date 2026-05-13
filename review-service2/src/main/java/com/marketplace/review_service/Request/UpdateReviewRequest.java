package com.marketplace.review_service.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateReviewRequest {
    @Min(value = 1, message = "rating must be at least 1")
    @Max(value = 5, message = "rating must be at most 5")
    private Integer rating;

    @Size(max = 200, message = "title must be at most 200 characters")
    private String title;

    @Size(min = 1, message = "content cannot be empty")
    private String content;

    private String pros;

    private String cons;

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPros() { return pros; }
    public void setPros(String pros) { this.pros = pros; }

    public String getCons() { return cons; }
    public void setCons(String cons) { this.cons = cons; }


}
