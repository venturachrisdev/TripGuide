
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period implements Parcelable
{

    @SerializedName("open")
    @Expose
    private Open open;
    public final static Creator<Period> CREATOR = new Creator<Period>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Period createFromParcel(Parcel in) {
            Period instance = new Period();
            instance.open = ((Open) in.readValue((Open.class.getClassLoader())));
            return instance;
        }

        public Period[] newArray(int size) {
            return (new Period[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Period() {
    }

    /**
     * 
     * @param open
     */
    public Period(Open open) {
        super();
        this.open = open;
    }

    public Open getOpen() {
        return open;
    }

    public void setOpen(Open open) {
        this.open = open;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(open);
    }

    public int describeContents() {
        return  0;
    }

}
