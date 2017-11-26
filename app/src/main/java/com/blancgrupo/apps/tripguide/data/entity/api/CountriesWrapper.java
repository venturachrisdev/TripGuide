package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class CountriesWrapper {
    @SerializedName("countries")
    @Expose
    List<Country> countries;

    @SerializedName("status")
    @Expose
    String status;

    public CountriesWrapper(List<Country> countries, String status) {
        this.countries = countries;
        this.status = status;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
