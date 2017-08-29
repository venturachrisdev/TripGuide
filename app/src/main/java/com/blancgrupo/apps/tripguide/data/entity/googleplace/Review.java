
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable
{

    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("author_url")
    @Expose
    private String authorUrl;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("profile_photo_url")
    @Expose
    private String profilePhotoUrl;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("relative_time_description")
    @Expose
    private String relativeTimeDescription;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time")
    @Expose
    private Integer time;
    public final static Creator<Review> CREATOR = new Creator<Review>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Review createFromParcel(Parcel in) {
            Review instance = new Review();
            instance.authorName = ((String) in.readValue((String.class.getClassLoader())));
            instance.authorUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.language = ((String) in.readValue((String.class.getClassLoader())));
            instance.profilePhotoUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.relativeTimeDescription = ((String) in.readValue((String.class.getClassLoader())));
            instance.text = ((String) in.readValue((String.class.getClassLoader())));
            instance.time = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Review[] newArray(int size) {
            return (new Review[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Review() {
    }

    /**
     * 
     * @param time
     * @param text
     * @param profilePhotoUrl
     * @param authorName
     * @param authorUrl
     * @param rating
     * @param language
     * @param relativeTimeDescription
     */
    public Review(String authorName, String authorUrl, String language, String profilePhotoUrl, Integer rating, String relativeTimeDescription, String text, Integer time) {
        super();
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.language = language;
        this.profilePhotoUrl = profilePhotoUrl;
        this.rating = rating;
        this.relativeTimeDescription = relativeTimeDescription;
        this.text = text;
        this.time = time;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(authorName);
        dest.writeValue(authorUrl);
        dest.writeValue(language);
        dest.writeValue(profilePhotoUrl);
        dest.writeValue(rating);
        dest.writeValue(relativeTimeDescription);
        dest.writeValue(text);
        dest.writeValue(time);
    }

    public int describeContents() {
        return  0;
    }

}
