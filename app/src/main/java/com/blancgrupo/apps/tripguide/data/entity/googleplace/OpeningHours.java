
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours implements Parcelable
{

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;
    public final static Creator<OpeningHours> CREATOR = new Creator<OpeningHours>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OpeningHours createFromParcel(Parcel in) {
            OpeningHours instance = new OpeningHours();
            instance.openNow = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            in.readList(instance.periods, (Period.class.getClassLoader()));
            in.readList(instance.weekdayText, (String.class.getClassLoader()));
            return instance;
        }

        public OpeningHours[] newArray(int size) {
            return (new OpeningHours[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OpeningHours() {
    }

    /**
     * 
     * @param weekdayText
     * @param periods
     * @param openNow
     */
    public OpeningHours(Boolean openNow, List<Period> periods, List<String> weekdayText) {
        super();
        this.openNow = openNow;
        this.periods = periods;
        this.weekdayText = weekdayText;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(openNow);
        dest.writeList(periods);
        dest.writeList(weekdayText);
    }

    public int describeContents() {
        return  0;
    }

}
