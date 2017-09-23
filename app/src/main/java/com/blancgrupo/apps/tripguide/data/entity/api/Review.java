package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class Review implements Parcelable {
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
    ReviewPlace place;
    @SerializedName("profile")
    @Expose
    ReviewProfile profile;

    public Review(Parcel in) {
        _id = in.readString();
        rating = in.readDouble();
        message = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public Review() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeDouble(rating);
        parcel.writeString(message);
        parcel.writeString(createdAt);
    }

    public static class ReviewPlace {
        @SerializedName("_id")
        @Expose
        String _id;
        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("address")
        @Expose
        String address;

        public ReviewPlace(String _id, String name, String address) {
            this._id = _id;
            this.name = name;
            this.address = address;
        }

        public ReviewPlace() {

        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class ReviewProfile {
        @SerializedName("_id")
        @Expose
        String _id;
        @SerializedName("tokenId")
        @Expose
        String tokenId;
        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("email")
        @Expose
        String email;
        @SerializedName("type")
        @Expose
        String type;
        @SerializedName("createdAt")
        @Expose
        String createdAt;
        @SerializedName("experience")
        @Expose
        int experience;
        @SerializedName("photoUrl")
        @Expose
        String photoUrl;

        public ReviewProfile(String _id, String tokenId, String name, String email, String type, String createdAt, int experience, String photoUrl) {
            this._id = _id;
            this.tokenId = tokenId;
            this.name = name;
            this.email = email;
            this.type = type;
            this.createdAt = createdAt;
            this.experience = experience;
            this.photoUrl = photoUrl;
        }

        public ReviewProfile() {

        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
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

    public ReviewPlace getPlace() {
        return place;
    }

    public void setPlace(ReviewPlace place) {
        this.place = place;
    }

    public ReviewProfile getProfile() {
        return profile;
    }

    public void setProfile(ReviewProfile profile) {
        this.profile = profile;
    }
}
