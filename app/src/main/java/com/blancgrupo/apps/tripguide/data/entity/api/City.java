package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 8/18/17.
 */

public class City implements Parcelable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("googleId")
    @Expose
    private String googleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent")
    private String parent;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("topics")
    @Expose
    private List<Topic> topics;


    public City(String id) {
        this.id = id;
    }

    public City(String id, String googleId, String name, Integer v, String createdAt, Photo photo, Location location, List<Topic> topics, String parent) {
        this.id = id;
        this.googleId = googleId;
        this.name = name;
        this.v = v;
        this.createdAt = createdAt;
        this.photo = photo;
        this.location = location;
        this.topics = topics;
        this.parent = parent;
    }

    protected City(Parcel in) {
        id = in.readString();
        googleId = in.readString();
        name = in.readString();
        parent = in.readString();
        createdAt = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(googleId);
        parcel.writeString(name);
        parcel.writeString(parent);
        parcel.writeString(createdAt);
        parcel.writeParcelable(photo, i);
        parcel.writeParcelable(location, i);
    }
}
