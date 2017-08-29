package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 8/18/17.
 */

public class Location implements Parcelable {
    @SerializedName("lng")
    @Expose
    private double lng;
    @SerializedName("lat")
    @Expose
    private double lat;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    protected Location(Parcel in) {
        lng = in.readDouble();
        lat = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lng);
        parcel.writeDouble(lat);
    }
}
