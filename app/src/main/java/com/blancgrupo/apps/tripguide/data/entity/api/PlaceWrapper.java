package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 8/18/17.
 */

public class PlaceWrapper {
    @SerializedName("place")
    @Expose
    Place place;

    @SerializedName("status")
    @Expose
    String status;

    public PlaceWrapper(Place place, String status) {
        this.place = place;
        this.status = status;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
