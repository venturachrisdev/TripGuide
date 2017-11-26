package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.blancgrupo.apps.tripguide.domain.repository.RegionRepository;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class RegionVMFactory implements ViewModelProvider.Factory {
    RegionRepository regionRepository;

    public RegionVMFactory(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public RegionViewModel create(Class modelClass) {
        return new RegionViewModel(regionRepository);
    }
}
