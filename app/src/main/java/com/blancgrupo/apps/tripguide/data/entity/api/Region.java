package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 11/9/17.
 */

public class Region {
    @SerializedName("_id")
    @Expose
    String _id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("country")
    @Expose
    Country country;
    @SerializedName("location")
    @Expose
    Location location;
    @SerializedName("photo")
    @Expose
    Photo photo;
    @SerializedName("createdAt")
    @Expose
    String createdAt;

    public Region() {}

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
