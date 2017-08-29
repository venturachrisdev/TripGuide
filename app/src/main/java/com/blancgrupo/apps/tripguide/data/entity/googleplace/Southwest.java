
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Southwest implements Parcelable
{

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    public final static Creator<Southwest> CREATOR = new Creator<Southwest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Southwest createFromParcel(Parcel in) {
            Southwest instance = new Southwest();
            instance.lat = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.lng = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public Southwest[] newArray(int size) {
            return (new Southwest[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Southwest() {
    }

    /**
     * 
     * @param lng
     * @param lat
     */
    public Southwest(Double lat, Double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(lat);
        dest.writeValue(lng);
    }

    public int describeContents() {
        return  0;
    }

}
