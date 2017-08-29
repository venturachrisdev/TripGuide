package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 8/18/17.
 */

public class CitiesWrapper {
    @SerializedName("cities")
    @Expose
    List<City> cities;

    @SerializedName("status")
    @Expose
    String status;

    public CitiesWrapper(List<City> cities, String status) {
        this.cities = cities;
        this.status = status;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
