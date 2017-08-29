package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by root on 8/18/17.
 */

public interface CityRepository {
    Observable<CitiesWrapper> getCities();
    Observable<CityWrapper> getSingleCity(String cityId);
}
