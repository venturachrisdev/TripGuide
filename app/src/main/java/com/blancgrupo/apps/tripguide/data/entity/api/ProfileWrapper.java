package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class ProfileWrapper {
    @SerializedName("profile")
    @Expose
    Profile profile;
    @SerializedName("status")
    @Expose
    String status;

    public ProfileWrapper(Profile profile, String status) {
        this.profile = profile;
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
