package com.example.futsalnepal.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Review {
    public String review;
    public float rating;
    public Date timeStamp;

    public Review() {
    }

    public Review(String review, float rating, Date timeStamp) {
        this.review = review;
        this.rating = rating;
        this.timeStamp = timeStamp;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
