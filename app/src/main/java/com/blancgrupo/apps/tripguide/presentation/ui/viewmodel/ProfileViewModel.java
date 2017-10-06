package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.ProfileLiveData;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class ProfileViewModel extends ViewModel {
    ProfileLiveData profileLiveData;
    ProfileRepository profileRepository;

    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public LiveData<ProfileWithReviews> signInOrRegister(Profile profile) {
        if (profileLiveData == null) {
            profileLiveData = new ProfileLiveData();
        }
        loadProfileForAccess(profile);
        return profileLiveData;
    }

    public LiveData<ProfileWithReviews> getLoggedProfile(String apiToken) {
        if (profileLiveData == null) {
            profileLiveData = new ProfileLiveData();
        }
        loadLoggedProfile(apiToken);
        return profileLiveData;
    }

    public boolean loadProfileForAccess(Profile profile) {
        if (profileLiveData != null) {
            profileLiveData.loadProfile(profileRepository.loginOrRegisterProfile(profile));
            return true;
        }
        return false;
    }

    public boolean loadLoggedProfile(String apiToken) {
        if (profileLiveData != null) {
            profileLiveData.loadProfile(profileRepository.getLoggedProfile(apiToken));
            return true;
        }
        return false;
    }

    public LiveData<ProfileWithReviews> getSingleProfile(String profileId) {
        if (profileLiveData == null) {
            profileLiveData = new ProfileLiveData();
        }
        loadSingleProfile(profileId);
        return profileLiveData;
    }

    public boolean loadSingleProfile(String profileId) {
        if (profileLiveData != null) {
            profileLiveData.loadProfile(profileRepository.getSingleProfile(profileId));
            return true;
        }
        return false;
    }
}
