package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 11/9/17.
 */

public class Country {
    @SerializedName("_id")
    @Expose
    String _id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("capital")
    @Expose
    String capital;
    @SerializedName("currency")
    @Expose
    String currency;
    @SerializedName("continent")
    @Expose
    String continent;
    @SerializedName("flagPhotoUrl")
    @Expose
    String flagPhotoUrl;
    @SerializedName("googleId")
    @Expose
    String googleId;
    @SerializedName("views")
    @Expose
    int views;
    @SerializedName("createdAt")
    @Expose
    String createdAt;
    @SerializedName("location")
    @Expose
    Location location;
    @SerializedName("photo")
    @Expose
    Photo photo;

    public Country() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getFlagPhotoUrl() {
        return flagPhotoUrl;
    }

    public void setFlagPhotoUrl(String flagPhotoUrl) {
        this.flagPhotoUrl = flagPhotoUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
