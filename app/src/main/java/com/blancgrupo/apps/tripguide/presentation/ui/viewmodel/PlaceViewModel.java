package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.PlaceLiveData;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.PlacesLiveData;

import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by root on 8/18/17.
 */

public class PlaceViewModel extends ViewModel {
    private GooglePlaceRepository googlePlaceRepository;
    private PlaceRepository placeRepository;
    private PlaceLiveData singlePlaceLiveData;
    private PlacesLiveData placesLiveData;
    private String placeId;
    private boolean fromGoogle = false;

    public PlaceViewModel(PlaceRepository placeRepository, GooglePlaceRepository googlePlaceRepository) {
        this.placeRepository = placeRepository;
        this.googlePlaceRepository = googlePlaceRepository;
    }

    public LiveData<PlaceWrapper> getSinglePlace(String placeId, boolean fromGoogle) {
        if (singlePlaceLiveData == null) {
            singlePlaceLiveData = new PlaceLiveData();
            loadSinglePlace(placeId, fromGoogle);
            this.placeId = placeId;
            this.fromGoogle = fromGoogle;
        } else {
            if (this.placeId == null || !this.placeId.equals(placeId)) {
                this.placeId = placeId;
                loadSinglePlace(this.placeId, this.fromGoogle);
            }
        }
        return singlePlaceLiveData;
    }

    public boolean isPlaceLoaded() {
        return this.placeId != null;
    }


    private void loadSinglePlace(String placeId, boolean fromGoogle) {
        if (singlePlaceLiveData != null) {
            if (fromGoogle) {
                singlePlaceLiveData.loadSinglePlace(
                        googlePlaceRepository.getPlaceDetail(placeId, Locale.getDefault().getLanguage())
                );

            } else {
                singlePlaceLiveData.loadSinglePlace(
                        placeRepository.getSinglePlace(placeId)
                );
            }
        }
    }

    public LiveData<PlacesWrapper> getPlaces() {
        if (placesLiveData == null) {
            placesLiveData = new PlacesLiveData();
            loadPlaces();
        }
        return placesLiveData;
    }

    private void loadPlaces() {
        if (placesLiveData != null) {
            placesLiveData.loadPlaces(placeRepository.getPlaces());
        }
    }

    public LiveData<PlaceWrapper> getLoadedSinglePlace() {
        return getSinglePlace(this.placeId, this.fromGoogle);
    }
}
