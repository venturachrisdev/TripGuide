package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 9/23/17.
 */

public class ReviewResponseWrapper {
    @SerializedName("review")
    @Expose
    ReviewResponse review;
    @SerializedName("status")
    @Expose
    String status;

    public ReviewResponseWrapper(ReviewResponse review, String status) {
        this.review = review;
        this.status = status;
    }

    public ReviewResponse getReview() {
        return review;
    }

    public void setReview(ReviewResponse review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class ReviewResponse {
        @SerializedName("_id")
        @Expose
        String _id;
        @SerializedName("rating")
        @Expose
        double rating;
        @SerializedName("message")
        @Expose
        String message;
        @SerializedName("createdAt")
        @Expose
        String createdAt;
        @SerializedName("place")
        @Expose
        String place;
        @SerializedName("profile")
        @Expose
        String profile;

        public ReviewResponse(String _id, double rating, String message, String createdAt, String place, String profile) {
            this._id = _id;
            this.rating = rating;
            this.message = message;
            this.createdAt = createdAt;
            this.place = place;
            this.profile = profile;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }
    }


}
