
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo implements Parcelable
{

    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions = null;
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;
    @SerializedName("width")
    @Expose
    private Integer width;
    public final static Creator<Photo> CREATOR = new Creator<Photo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Photo createFromParcel(Parcel in) {
            Photo instance = new Photo();
            instance.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.htmlAttributions, (String.class.getClassLoader()));
            instance.photoReference = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Photo[] newArray(int size) {
            return (new Photo[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Photo() {
    }

    /**
     * 
     * @param height
     * @param width
     * @param htmlAttributions
     * @param photoReference
     */
    public Photo(Integer height, List<String> htmlAttributions, String photoReference, Integer width) {
        super();
        this.height = height;
        this.htmlAttributions = htmlAttributions;
        this.photoReference = photoReference;
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(height);
        dest.writeList(htmlAttributions);
        dest.writeValue(photoReference);
        dest.writeValue(width);
    }

    public int describeContents() {
        return  0;
    }

}
