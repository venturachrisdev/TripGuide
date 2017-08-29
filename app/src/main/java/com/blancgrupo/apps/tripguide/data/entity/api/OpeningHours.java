package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 8/18/17.
 */

public class OpeningHours {
    @SerializedName("weekdays")
    @Expose
    private List<String> weekdays = null;
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;


    public OpeningHours(List<String> weekdays, boolean openNow) {
        this.weekdays = weekdays;
        this.openNow = openNow;
    }

    public List<String> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<String> weekdays) {
        this.weekdays = weekdays;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
