package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.domain.repository.CityRepository;

import javax.inject.Inject;

/**
 * Created by root on 8/18/17.
 */

public class CityVMFactory implements ViewModelProvider.Factory {
    private CityRepository cityRepository;

    @Inject
    public CityVMFactory(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityViewModel create(Class modelClass) {
        return new CityViewModel(this.cityRepository);
    }
}
