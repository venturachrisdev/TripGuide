package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 8/18/17.
 */

public class Topic {
    @SerializedName("_id")
    @Expose
    String _id;
    @SerializedName("places")
    @Expose
    List<PlaceCover> places;

    public Topic(String _id, List<PlaceCover> places) {
        this._id = _id;
        this.places = places;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<PlaceCover> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceCover> places) {
        this.places = places;
    }
}
