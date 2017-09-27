package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.UploadPhotoWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

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
    public Observable<UploadPhotoWrapper> uploadPhoto(MultipartBody.Part image, String apiToken) {
        return apiPlaceService.uploadPhoto(image, apiToken);
    }

    @Override
    public Observable<ReviewResponseWrapper> addReview(Review review, String apiToken) {
        return apiPlaceService.addReview(review, apiToken);
    }

    @Override
    public Observable<String> changeProfilePhoto(Profile profile, String apiToken) {
        return apiPlaceService.changeProfilePhoto(profile, apiToken);
    }
}
