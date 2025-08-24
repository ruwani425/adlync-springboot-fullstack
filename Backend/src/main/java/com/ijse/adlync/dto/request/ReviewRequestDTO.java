package com.ijse.adlync.dto.request;

import java.time.LocalDateTime;

public class ReviewRequestDTO {

    private Double rating;
    private LocalDateTime created_at;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
