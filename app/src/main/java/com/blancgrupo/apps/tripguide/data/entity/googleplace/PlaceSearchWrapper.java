
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceSearchWrapper implements Parcelable
{

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    private List<PlaceSearch> results = null;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<PlaceSearchWrapper> CREATOR = new Creator<PlaceSearchWrapper>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PlaceSearchWrapper createFromParcel(Parcel in) {
            PlaceSearchWrapper instance = new PlaceSearchWrapper();
            in.readList(instance.htmlAttributions, (Object.class.getClassLoader()));
            in.readList(instance.results, (PlaceSearch.class.getClassLoader()));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PlaceSearchWrapper[] newArray(int size) {
            return (new PlaceSearchWrapper[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PlaceSearchWrapper() {
    }

    /**
     * 
     * @param results
     * @param status
     * @param htmlAttributions
     */
    public PlaceSearchWrapper(List<Object> htmlAttributions, List<PlaceSearch> results, String status) {
        super();
        this.htmlAttributions = htmlAttributions;
        this.results = results;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<PlaceSearch> getResults() {
        return results;
    }

    public void setResults(List<PlaceSearch> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(htmlAttributions);
        dest.writeList(results);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
