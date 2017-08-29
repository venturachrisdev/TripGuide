package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceDetailWrapper;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceSearchWrapper;
import com.blancgrupo.apps.tripguide.data.service.GooglePlaceService;
import com.blancgrupo.apps.tripguide.domain.mapper.GooglePlaceMapper;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by root on 8/15/17.
 */

public class GooglePlaceRepository {
    GooglePlaceService googlePlaceService;

    @Inject
    public GooglePlaceRepository(GooglePlaceService googlePlaceService) {
        this.googlePlaceService = googlePlaceService;
    }

    public Observable<PlaceWrapper> getPlaceDetail(final String placeId, String lang) {
        return googlePlaceService.getPlaceDetail(placeId, lang)
                .map(new Function<PlaceDetailWrapper, PlaceWrapper>() {
                    @Override
                    public PlaceWrapper apply(@NonNull PlaceDetailWrapper placeDetailWrapper) throws Exception {
                        return new PlaceWrapper(
                                GooglePlaceMapper.transformDetail(placeDetailWrapper.getResult()),
                                placeDetailWrapper.getStatus()
                        );
                    }
                });
    }

    public Observable<PlacesCoverWrapper> searchPlaceByType(String query, String type, String lang) {
        return googlePlaceService.searchPlaceByType(query, type, lang)
                .map(new Function<PlaceSearchWrapper, PlacesCoverWrapper>() {
                    @Override
                    public PlacesCoverWrapper apply(@NonNull PlaceSearchWrapper placeSearchWrapper) throws Exception {
                        return new PlacesCoverWrapper(
                               GooglePlaceMapper.transformCoverFromSearch(placeSearchWrapper.getResults()),
                                placeSearchWrapper.getStatus()
                        );
                    }
                });
    }

    public Observable<PlacesCoverWrapper> searchNearBy(String location, String language,String type, int radius) {
        return googlePlaceService.searchNearBy(location, language, type, radius)
                .map(new Function<PlaceSearchWrapper, PlacesCoverWrapper>() {
                    @Override
                    public PlacesCoverWrapper apply(@NonNull PlaceSearchWrapper placeSearchWrapper) throws Exception {
                        return new PlacesCoverWrapper(
                                GooglePlaceMapper.transformCoverFromSearch(placeSearchWrapper.getResults()),
                                placeSearchWrapper.getStatus()
                        );
                    }
                });

    }

}
