package com.blancgrupo.apps.tripguide.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by venturachrisdev on 10/3/17.
 */

@Entity(tableName = "reviews")
public class ReviewModel {
    @PrimaryKey
    @NonNull
    private String _id;
    private double rating;
    private String message;
    private String createdAt;
    private String photo;
    private String placeId;
    private String profileId;


    public ReviewModel(String _id, double rating, String message, String createdAt, String photo, String placeId, String profileId) {
        this._id = _id;
        this.rating = rating;
        this.message = message;
        this.createdAt = createdAt;
        this.photo = photo;
        this.placeId = placeId;
        this.profileId = profileId;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
