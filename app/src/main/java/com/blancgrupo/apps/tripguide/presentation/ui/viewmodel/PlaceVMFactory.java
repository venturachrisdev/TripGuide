package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;

import javax.inject.Inject;

/**
 * Created by root on 8/21/17.
 */

public class PlaceVMFactory implements ViewModelProvider.Factory {
    private PlaceRepository placeRepository;
    private GooglePlaceRepository googlePlaceRepository;

    @Inject
    public PlaceVMFactory(PlaceRepository placeRepository, GooglePlaceRepository googlePlaceRepository) {
        this.placeRepository = placeRepository;
        this.googlePlaceRepository = googlePlaceRepository;
    }

    @Override
    public PlaceViewModel create(Class modelClass) {
        return new PlaceViewModel(this.placeRepository, this.googlePlaceRepository);
    }
}
