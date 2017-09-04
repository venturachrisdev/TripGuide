package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.CityRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by root on 8/18/17.
 */

public class ApiCityRepository implements CityRepository {
    ApiPlaceService apiPlaceService;

    @Inject
    public ApiCityRepository(ApiPlaceService apiPlaceService) {
        this.apiPlaceService = apiPlaceService;
    }

    @Override
    public Observable<CitiesWrapper> getCities() {
        return apiPlaceService.getCities();
    }

    @Override
    public Observable<CityWrapper> getSingleCity(String cityId) {
        return apiPlaceService.getSingleCity(cityId);
    }

    @Override
    public Observable<String> getCurrentCity(String lat, String lng) {
        return apiPlaceService.getCurrentCity(lat, lng);
    }
}
