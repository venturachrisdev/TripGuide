package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;

import io.reactivex.Observable;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class ApiProfileRepository implements ProfileRepository {
    ApiPlaceService apiPlaceService;

    public ApiProfileRepository(ApiPlaceService apiPlaceService) {
        this.apiPlaceService = apiPlaceService;
    }

    @Override
    public Observable<ProfileWrapper> getSingleProfile(String profileId) {
        return apiPlaceService.getSingleProfile(profileId);
    }

    @Override
    public Observable<ProfileWrapper> loginOrRegisterProfile(Profile profile) {
        return apiPlaceService.loginOrRegisterProfile(profile);
    }

    @Override
    public Observable<ReviewResponseWrapper> addReview(Review review) {
        return apiPlaceService.addReview(review);
    }
}
