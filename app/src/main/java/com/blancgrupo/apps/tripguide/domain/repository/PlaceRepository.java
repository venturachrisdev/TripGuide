package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.PlaceDescriptionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;

import io.reactivex.Observable;

/**
 * Created by root on 8/18/17.
 */

public interface PlaceRepository {
    Observable<PlacesWrapper> getPlaces();
    Observable<PlaceWrapper> getSinglePlace(String placeId, String apiToken);
    Observable<PlaceDescriptionWrapper>
    getPlaceDescription(String placeId, String lang);
}
