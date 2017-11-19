package com.blancgrupo.apps.tripguide.data.entity.api;

/**
 * Created by venturachrisdev on 11/9/17.
 */

public class RegionWrapper {
    Region region;
    String status;

    public RegionWrapper() {}

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
