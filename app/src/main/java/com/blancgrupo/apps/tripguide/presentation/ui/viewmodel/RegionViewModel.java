package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.entity.api.RegionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.RegionsWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.RegionRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.RegionLiveData;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.RegionsLiveData;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class RegionViewModel extends ViewModel {
    RegionRepository regionRepository;
    RegionLiveData regionLiveData;
    RegionsLiveData regionsLiveData;

    public RegionViewModel(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public LiveData<RegionWrapper> getRegion(String apiToken, String regionId) {
        if (regionLiveData == null) {
            regionLiveData = new RegionLiveData();
        }
        loadRegion(apiToken, regionId);
        return regionLiveData;
    }

    public void loadRegion(String apiToken, String regionId) {
        regionLiveData.getRegion(regionRepository.getRegion(apiToken, regionId));
    }

    public LiveData<RegionsWrapper> getRegions(String apiToken) {
        if (regionsLiveData == null) {
            regionsLiveData = new RegionsLiveData();
        }
        loadRegions(apiToken);
        return regionsLiveData;
    }

    public void loadRegions(String apiToken) {
        regionsLiveData.getRegions(regionRepository.getRegions(apiToken));
    }
}
