package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceDescriptionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by root on 8/18/17.
 */

public interface PlaceRepository {
    Observable<PlacesWrapper> getPlaces();
    Observable<PlaceWrapper> getSinglePlace(String placeId);
    Observable<PlacesWrapper> getTours();
    Observable<PlaceDescriptionWrapper>
    getPlaceDescription(String placeId, String lang);
}
