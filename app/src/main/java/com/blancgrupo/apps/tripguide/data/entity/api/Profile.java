package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class Profile implements Parcelable {
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
    @SerializedName("reviews")
    @Expose
    List<Review> reviews;

    public Profile(String _id, String tokenId, String name, String email, String type, String createdAt, int experience, String photoUrl, List<Review> reviews) {
        this._id = _id;
        this.tokenId = tokenId;
        this.name = name;
        this.email = email;
        this.type = type;
        this.createdAt = createdAt;
        this.experience = experience;
        this.photoUrl = photoUrl;
        this.reviews = reviews;
    }

    protected Profile(Parcel in) {
        _id = in.readString();
        tokenId = in.readString();
        name = in.readString();
        email = in.readString();
        type = in.readString();
        createdAt = in.readString();
        experience = in.readInt();
        photoUrl = in.readString();
        reviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public Profile() {

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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(tokenId);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(type);
        parcel.writeString(createdAt);
        parcel.writeInt(experience);
        parcel.writeString(photoUrl);
        parcel.writeTypedList(reviews);
    }
}
