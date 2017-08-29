package com.blancgrupo.apps.tripguide.data.entity.api;


import java.util.List;

/**
 * Created by root on 8/25/17.
 */

public class PlacesCoverWrapper {
    List<PlaceCover> places;
    String status;

    public PlacesCoverWrapper(List<PlaceCover> places, String status) {
        this.places = places;
        this.status = status;
    }

    public List<PlaceCover> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceCover> places) {
        this.places = places;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
