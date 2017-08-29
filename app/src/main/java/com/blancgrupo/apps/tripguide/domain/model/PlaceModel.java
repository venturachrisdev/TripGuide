package com.blancgrupo.apps.tripguide.domain.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by root on 8/15/17.
 */

@Entity(tableName = "places")
public class PlaceModel {
    @PrimaryKey
    private String placeId;
    private String googleId;
    private String name;
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
    private String createdAt;

    @Ignore
    public PlaceModel() {
    }

    public PlaceModel(String placeId, String googleId, String name, String address, double lat, double lng, String phoneNumber, String type, String photoUrl, String city, String website, boolean openNow, String weekdays, long distance, String createdAt) {
        this.placeId = placeId;
        this.googleId = googleId;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.photoUrl = photoUrl;
        this.city = city;
        this.website = website;
        this.openNow = openNow;
        this.weekdays = weekdays;
        this.distance = distance;
        this.createdAt = createdAt;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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
}
