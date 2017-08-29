package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.CityRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.CitiesLiveData;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.CityLiveData;

/**
 * Created by root on 8/18/17.
 */

public class CityViewModel extends ViewModel {
    private CityRepository cityRepository;
    private CityLiveData singleCityLiveData;
    private CitiesLiveData citiesLiveData;
    private String cityId;

    public CityViewModel(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public LiveData<CityWrapper> getSingleCity(String cityId) {
        if (singleCityLiveData == null) {
            singleCityLiveData = new CityLiveData();
            loadSingleCity(cityId);
            this.cityId = cityId;
        } else {
            if (this.cityId == null || !this.cityId.equals(cityId)) {
                this.cityId = cityId;
                loadSingleCity(this.cityId);
            }
        }
        return singleCityLiveData;
    }

    public boolean isSingleCityLoaded() {
        return cityId != null;
    };

    public void loadSingleCity(String cityId) {
        if (singleCityLiveData != null) {
            singleCityLiveData.loadSingleCity(cityRepository.getSingleCity(cityId));
        }
    }

    public LiveData<CitiesWrapper> getCities() {
        if (citiesLiveData == null) {
            citiesLiveData = new CitiesLiveData();
            loadCities();
        }
        return citiesLiveData;
    }

    public void loadCities() {
        if (citiesLiveData != null) {
            citiesLiveData.loadCities(cityRepository.getCities());
        }
    }

    public LiveData<CityWrapper> getLoadedSingleCity() {
        return getSingleCity(this.cityId);
    }

    public void loadSavedSingleCity() {
        loadSingleCity(this.cityId);
    }
}
