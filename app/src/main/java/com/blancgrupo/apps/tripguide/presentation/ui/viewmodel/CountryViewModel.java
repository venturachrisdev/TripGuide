package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.entity.api.CountriesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CountryWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.CountryRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.CountriesLiveData;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.CountryLiveData;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class CountryViewModel extends ViewModel {
    CountryRepository countryRepository;
    CountryLiveData countryLiveData;
    CountriesLiveData countriesLiveData;

    public CountryViewModel(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public LiveData<CountryWrapper> getCountry(String apiToken, String countryId) {
        if (countryLiveData == null) {
            countryLiveData = new CountryLiveData();
        }
        loadCountry(apiToken, countryId);
        return countryLiveData;
    }

    public void loadCountry(String apiToken, String countryId) {
        countryLiveData.getCountry(countryRepository.getCountry(apiToken, countryId));
    }

    public LiveData<CountriesWrapper> getCountries(String apiToken) {
        if (countriesLiveData == null) {
            countriesLiveData = new CountriesLiveData();
        }
        loadCountries(apiToken);
        return countriesLiveData;
    }

    public void loadCountries(String apiToken) {
        countriesLiveData.getCountries(countryRepository.getCountries(apiToken));
    }


}
