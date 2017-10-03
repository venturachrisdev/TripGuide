package com.blancgrupo.apps.tripguide.domain.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by venturachrisdev on 10/3/17.
 */

public class ProfileWithReviews {
    @Embedded
    private ProfileModel profile;
    @Relation(parentColumn = "_id", entityColumn = "profileId", entity = ReviewModel.class)
    private List<ReviewModel> reviews;

    public ProfileWithReviews() {
    }

    public ProfileModel getProfile() {
        return profile;
    }

    public void setProfile(ProfileModel profile) {
        this.profile = profile;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }
}
