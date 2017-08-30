package com.blancgrupo.apps.tripguide.data.entity.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 8/18/17.
 */

public class Photo implements Parcelable {

    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName(value = "reference", alternate = {"photo_reference"})
    @Expose
    private String reference;

    public Photo(String reference, int width) {
        this.reference = reference;
        this.width = width;
    }

    protected Photo(Parcel in) {
        width = in.readInt();
        reference = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(width);
        parcel.writeString(reference);
    }
}
