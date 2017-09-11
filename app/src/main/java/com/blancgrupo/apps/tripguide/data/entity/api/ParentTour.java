package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 9/6/17.
 */

public class ParentTour {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("googleId")
    @Expose
    private String googleId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos;
    @SerializedName("tours")
    @Expose
    private List<TourCover> tours = null;

    public ParentTour(String name, City city, String googleId, String address, String id, Photo photo, List<TourCover> tours, List<Photo> photos) {
        this.name = name;
        this.city = city;
        this.googleId = googleId;
        this.address = address;
        this.id = id;
        this.photo = photo;
        this.tours = tours;
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public List<TourCover> getTours() {
        return tours;
    }

    public void setTours(List<TourCover> tours) {
        this.tours = tours;
    }

}