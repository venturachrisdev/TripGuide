
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Open implements Parcelable
{

    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("time")
    @Expose
    private String time;
    public final static Creator<Open> CREATOR = new Creator<Open>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Open createFromParcel(Parcel in) {
            Open instance = new Open();
            instance.day = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.time = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Open[] newArray(int size) {
            return (new Open[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Open() {
    }

    /**
     * 
     * @param time
     * @param day
     */
    public Open(Integer day, String time) {
        super();
        this.day = day;
        this.time = time;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(day);
        dest.writeValue(time);
    }

    public int describeContents() {
        return  0;
    }

}
