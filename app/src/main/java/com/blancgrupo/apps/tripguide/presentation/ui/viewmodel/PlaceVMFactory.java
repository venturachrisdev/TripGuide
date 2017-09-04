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

    @Inject
    public PlaceVMFactory(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public PlaceViewModel create(Class modelClass) {
        return new PlaceViewModel(this.placeRepository);
    }
}
