package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.domain.repository.TourRepository;

import javax.inject.Inject;

/**
 * Created by root on 8/29/17.
 */

public class TourVMFactory implements ViewModelProvider.Factory {
    private TourRepository tourRepository;

    @Inject
    public TourVMFactory(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Override
    public TourViewModel create(Class modelClass) {
        return new TourViewModel(this.tourRepository);
    }
}
