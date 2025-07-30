package com.ivision.model;

import java.io.Serializable;

public class Review implements Serializable {

    String review;
    String date;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
