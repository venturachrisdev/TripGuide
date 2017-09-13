package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 9/6/17.
 */

public class Tour implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("totalDistance")
    @Expose
    private Integer totalDistance;
    @SerializedName("totalTime")
    @Expose
    private Integer totalTime;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("places")
    @Expose
    private List<PlaceTypesCover> places = null;

    public Tour(String id, String parent, String name, Integer totalDistance, Integer totalTime, Integer v, String createdAt, List<PlaceTypesCover> places) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.v = v;
        this.createdAt = createdAt;
        this.places = places;
    }

    protected Tour(Parcel in) {
        id = in.readString();
        parent = in.readString();
        name = in.readString();
        createdAt = in.readString();
        places = in.createTypedArrayList(PlaceTypesCover.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(parent);
        dest.writeString(name);
        dest.writeString(createdAt);
        dest.writeTypedList(places);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tour> CREATOR = new Creator<Tour>() {
        @Override
        public Tour createFromParcel(Parcel in) {
            return new Tour(in);
        }

        @Override
        public Tour[] newArray(int size) {
            return new Tour[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<PlaceTypesCover> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceTypesCover> places) {
        this.places = places;
    }

}