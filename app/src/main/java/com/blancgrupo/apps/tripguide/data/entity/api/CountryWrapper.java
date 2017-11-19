package com.blancgrupo.apps.tripguide.data.entity.api;

/**
 * Created by venturachrisdev on 11/9/17.
 */

public class CountryWrapper {
    Country country;
    String status;

    public CountryWrapper() {

    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
