package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venturachrisdev on 9/25/17.
 */

public class UploadPhotoWrapper {
    @SerializedName("photo")
    @Expose
    private String photoUrl;
    @SerializedName("status")
    @Expose
    private String status;

    public UploadPhotoWrapper(String photoUrl, String status) {
        this.photoUrl = photoUrl;
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
