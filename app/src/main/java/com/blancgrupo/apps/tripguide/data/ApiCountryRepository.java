package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.CountriesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CountryWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.CountryRepository;

import io.reactivex.Observable;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class ApiCountryRepository implements CountryRepository {
    ApiPlaceService apiPlaceService;


    public ApiCountryRepository(ApiPlaceService apiPlaceService) {
        this.apiPlaceService = apiPlaceService;
    }

    @Override
    public Observable<CountryWrapper> getCountry(String apiToken, String countryId) {
        return apiPlaceService.getCountry(apiToken, countryId);
    }

    @Override
    public Observable<CountriesWrapper> getCountries(String apiToken) {
        return apiPlaceService.getCountries(apiToken);
    }
}
