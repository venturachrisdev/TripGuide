package com.blancgrupo.apps.tripguide.domain.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by venturachrisdev on 10/3/17.
 */

public class PlaceWithReviews {
    @Embedded
    private PlaceModel place;
    @Relation(parentColumn = "_id", entityColumn = "_id", entity = ReviewModel.class)
    private List<ReviewModel> reviews;

    public PlaceWithReviews() {
    }

    public PlaceModel getPlace() {
        return place;
    }

    public void setPlace(PlaceModel place) {
        this.place = place;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }
}
