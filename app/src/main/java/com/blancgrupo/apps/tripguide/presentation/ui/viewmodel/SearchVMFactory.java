package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;

import javax.inject.Inject;

/**
 * Created by root on 8/18/17.
 */

public class SearchVMFactory implements ViewModelProvider.Factory {
    private GooglePlaceRepository googlePlaceRepository;

    @Inject
    public SearchVMFactory(GooglePlaceRepository googlePlaceRepository) {
        this.googlePlaceRepository = googlePlaceRepository;
    }

    @Override
    public SearchViewModel create(Class modelClass) {
        return new SearchViewModel(this.googlePlaceRepository);
    }
}
