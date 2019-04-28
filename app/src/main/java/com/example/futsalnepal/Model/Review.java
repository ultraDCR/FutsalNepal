package com.example.futsalnepal.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Review {
    public String review;
    public Number rating;
    public Date timestamp;

    public Review() {
    }

    public Review(String review, Number rating, Date timestamp) {
        this.review = review;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Number getRating() {
        return rating;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
