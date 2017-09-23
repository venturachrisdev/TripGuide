package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class ProfileVMFactory implements ViewModelProvider.Factory {
    ProfileRepository profileRepository;

    public ProfileVMFactory(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileViewModel create(Class modelClass) {
        return new ProfileViewModel(profileRepository);
    }
}
