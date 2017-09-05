package com.blancgrupo.apps.tripguide.data.entity.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/5/17.
 */

public class PlaceDescription {
    @SerializedName("_id")
    @Expose
    String id;
    @SerializedName("place")
    @Expose
    String place;
    @SerializedName("lang")
    @Expose
    String lang;
    @SerializedName("text")
    @Expose
    String text;
    @SerializedName("createdAt")
    @Expose
    String createdAt;

    public PlaceDescription(String text) {
        this.text = text;
    }

    public PlaceDescription(String id, String place, String lang, String text, String createdAt) {
        this.id = id;
        this.place = place;
        this.lang = lang;
        this.text = text;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
