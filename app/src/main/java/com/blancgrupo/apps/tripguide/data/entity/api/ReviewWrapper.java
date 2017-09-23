package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class ReviewWrapper {
    @SerializedName("review")
    @Expose
    Review review;
    @SerializedName("status")
    @Expose
    String status;

    public ReviewWrapper(Review review, String status) {
        this.review = review;
        this.status = status;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
