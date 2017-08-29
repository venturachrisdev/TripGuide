
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceDetailWrapper implements Parcelable
{

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private PlaceDetail result;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<PlaceDetailWrapper> CREATOR = new Creator<PlaceDetailWrapper>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PlaceDetailWrapper createFromParcel(Parcel in) {
            PlaceDetailWrapper instance = new PlaceDetailWrapper();
            in.readList(instance.htmlAttributions, (Object.class.getClassLoader()));
            instance.result = ((PlaceDetail) in.readValue((PlaceDetail.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PlaceDetailWrapper[] newArray(int size) {
            return (new PlaceDetailWrapper[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PlaceDetailWrapper() {
    }

    /**
     * 
     * @param result
     * @param status
     * @param htmlAttributions
     */
    public PlaceDetailWrapper(List<Object> htmlAttributions, PlaceDetail result, String status) {
        super();
        this.htmlAttributions = htmlAttributions;
        this.result = result;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public PlaceDetail getResult() {
        return result;
    }

    public void setResult(PlaceDetail result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(htmlAttributions);
        dest.writeValue(result);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
