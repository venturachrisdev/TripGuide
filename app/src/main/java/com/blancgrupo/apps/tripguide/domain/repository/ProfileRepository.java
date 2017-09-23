package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewWrapper;

import io.reactivex.Observable;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public interface ProfileRepository {
    Observable<ProfileWrapper> getSingleProfile(String profileId);
    Observable<ProfileWrapper> loginOrRegisterProfile(Profile profile);
    Observable<ReviewResponseWrapper> addReview(Review review);
}
