package com.blancgrupo.apps.tripguide.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by root on 8/15/17.
 */

@Entity(tableName = "places")
public class PlaceModel implements Parcelable {
    @PrimaryKey
    @NonNull
    private String _id;
    private String googleId;
    private String name;
    private String description;
    private String address;
    private double lat;
    private double lng;
    private String phoneNumber;
    private String type;
    private String photoUrl;
    private String city;
    private String website;
    private boolean openNow;
    private String weekdays;
    private long distance;
    private double rating;
    private String createdAt;
    private boolean isFavorite;
    private boolean userHasReviewed;
    String cityId;

    public PlaceModel() {
    }

    protected PlaceModel(Parcel in) {
        _id = in.readString();
        googleId = in.readString();
        name = in.readString();
        description = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        phoneNumber = in.readString();
        type = in.readString();
        photoUrl = in.readString();
        city = in.readString();
        website = in.readString();
        openNow = in.readByte() != 0;
        weekdays = in.readString();
        distance = in.readLong();
        rating = in.readDouble();
        createdAt = in.readString();
        isFavorite = in.readByte() != 0;
        userHasReviewed = in.readByte() != 0;
        cityId = in.readString();
    }

    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel in) {
            return new PlaceModel(in);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isUserHasReviewed() {
        return userHasReviewed;
    }

    public void setUserHasReviewed(boolean userHasReviewed) {
        this.userHasReviewed = userHasReviewed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public String getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(googleId);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(address);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(phoneNumber);
        parcel.writeString(type);
        parcel.writeString(photoUrl);
        parcel.writeString(city);
        parcel.writeString(website);
        parcel.writeByte((byte) (openNow ? 1 : 0));
        parcel.writeString(weekdays);
        parcel.writeLong(distance);
        parcel.writeDouble(rating);
        parcel.writeString(createdAt);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeByte((byte) (userHasReviewed ? 1 : 0));
        parcel.writeString(cityId);
    }
}
