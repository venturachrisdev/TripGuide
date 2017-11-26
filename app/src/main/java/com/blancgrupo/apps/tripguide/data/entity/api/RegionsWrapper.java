package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class RegionsWrapper {
    @SerializedName("regions")
    @Expose
    List<Region> regions;

    @SerializedName("status")
    @Expose
    String status;

    public RegionsWrapper(List<Region> regions, String status) {
        this.regions = regions;
        this.status = status;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
