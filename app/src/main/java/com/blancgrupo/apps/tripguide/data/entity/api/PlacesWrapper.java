package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 8/18/17.
 */

public class PlacesWrapper {
    @SerializedName("places")
    @Expose
    List<Place> places;

    @SerializedName("status")
    @Expose
    String status;

    public PlacesWrapper(List<Place> places, String status) {
        this.places = places;
        this.status = status;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
