package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.ApiProfileRepository;
import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
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

    public LiveData<ProfileWrapper> signInOrRegister(Profile profile) {
        if (profileLiveData == null) {
            profileLiveData = new ProfileLiveData();
        }
        loadProfileForAccess(profile);
        return profileLiveData;
    }

    public void loadProfileForAccess(Profile profile) {
        if (profileLiveData != null) {
            profileLiveData.loadProfile(profileRepository.loginOrRegisterProfile(profile));
        }
    }
}
