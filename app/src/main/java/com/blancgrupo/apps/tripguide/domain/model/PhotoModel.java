package com.blancgrupo.apps.tripguide.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by venturachrisdev on 10/4/17.
 */

@Entity(tableName = "photos")
public class PhotoModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    Long _id;
    String reference;
    int width;
    String placeId;

    public PhotoModel() {
    }

    @NonNull
    public Long get_id() {
        return _id;
    }

    public void set_id(@NonNull Long _id) {
        this._id = _id;
    }

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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
