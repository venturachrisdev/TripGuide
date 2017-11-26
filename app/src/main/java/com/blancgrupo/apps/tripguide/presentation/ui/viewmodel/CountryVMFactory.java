package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.domain.repository.CountryRepository;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class CountryVMFactory implements ViewModelProvider.Factory {
    CountryRepository countryRepository;

    public CountryVMFactory(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryViewModel create(Class modelClass) {
        return new CountryViewModel(countryRepository);
    }
}
