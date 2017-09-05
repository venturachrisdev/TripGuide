package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/5/17.
 */

public class PlaceDescriptionWrapper {
    @SerializedName("info")
    @Expose
    PlaceDescription placeDescription;
    @SerializedName("status")
    @Expose
    String status;

    public PlaceDescriptionWrapper(PlaceDescription placeDescription, String status) {
        this.placeDescription = placeDescription;
        this.status = status;
    }

    public PlaceDescription getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(PlaceDescription placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
