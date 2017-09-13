package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 9/12/17.
 */

public class PlaceTypesCover implements Parcelable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("types")
    @Expose
    private List<String> types;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("googleId")
    @Expose
    private String googleId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("phone_number")
    private String phoneNumber;

    public PlaceTypesCover(String id, List<String> types, String address, String googleId, String city, String name, Integer v, String createdAt, OpeningHours openingHours, Photo photo, Location location, String phoneNumber) {
        this.id = id;
        this.types = types;
        this.address = address;
        this.googleId = googleId;
        this.city = city;
        this.name = name;
        this.v = v;
        this.createdAt = createdAt;
        this.openingHours = openingHours;
        this.photo = photo;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    protected PlaceTypesCover(Parcel in) {
        id = in.readString();
        types = in.createStringArrayList();
        address = in.readString();
        googleId = in.readString();
        city = in.readString();
        name = in.readString();
        createdAt = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
        phoneNumber = in.readString();
    }

    public static final Creator<PlaceTypesCover> CREATOR = new Creator<PlaceTypesCover>() {
        @Override
        public PlaceTypesCover createFromParcel(Parcel in) {
            return new PlaceTypesCover(in);
        }

        @Override
        public PlaceTypesCover[] newArray(int size) {
            return new PlaceTypesCover[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeStringList(types);
        parcel.writeString(address);
        parcel.writeString(googleId);
        parcel.writeString(city);
        parcel.writeString(name);
        parcel.writeString(createdAt);
        parcel.writeParcelable(photo, i);
        parcel.writeParcelable(location, i);
        parcel.writeString(phoneNumber);
    }
}
