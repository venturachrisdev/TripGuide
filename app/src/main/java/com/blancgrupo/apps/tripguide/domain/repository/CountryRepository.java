package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.CountriesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CountryWrapper;

import io.reactivex.Observable;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public interface CountryRepository {
    Observable<CountryWrapper> getCountry(String apiToken, String countryId);
    Observable<CountriesWrapper> getCountries(String apiToken);
}
