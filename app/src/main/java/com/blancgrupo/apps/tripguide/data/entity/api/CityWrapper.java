package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 8/18/17.
 */

public class CityWrapper {
    @SerializedName("city")
    @Expose
    City city;
    @SerializedName("status")
    @Expose
    String status;

    public CityWrapper(City city, String status) {
        this.city = city;
        this.status = status;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
