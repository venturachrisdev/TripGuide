package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.UploadPhotoWrapper;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public interface ProfileRepository {
    Observable<ProfileWrapper> getSingleProfile(String profileId);
    Observable<ProfileWrapper> getLoggedProfile(String apiToken);
    Observable<ProfileWrapper> loginOrRegisterProfile(Profile profile);
    Observable<UploadPhotoWrapper> uploadPhoto(MultipartBody.Part image, String apiToken);
    Observable<ReviewResponseWrapper> addReview(Review review, String apiToken);
    Observable<String> changeProfilePhoto(Profile profile, String apiToken);
}
