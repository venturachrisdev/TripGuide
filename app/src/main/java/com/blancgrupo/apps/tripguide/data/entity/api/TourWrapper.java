package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/6/17.
 */

public class TourWrapper {

    @SerializedName("tour")
    @Expose
    private Tour tour;
    @SerializedName("status")
    @Expose
    private String status;

    public TourWrapper(Tour tour, String status) {
        this.tour = tour;
        this.status = status;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}