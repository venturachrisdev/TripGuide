package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.PlacesCoverLiveData;

import java.util.Locale;

/**
 * Created by root on 8/15/17.
 */

public class SearchViewModel extends ViewModel {
    GooglePlaceRepository googlePlaceRepository;
    PlacesCoverLiveData placesCoverLiveData;

    public SearchViewModel(GooglePlaceRepository googlePlaceRepository) {
        this.googlePlaceRepository = googlePlaceRepository;
    }

    public LiveData<PlacesCoverWrapper> searchPlacesByType(String query, String type) {
        if (placesCoverLiveData== null) {
            placesCoverLiveData = new PlacesCoverLiveData();
            if (query != null && type != null) {
                loadPlacesByType(query, type);
            }
        }
        return placesCoverLiveData;
    }

    public void loadPlacesByType(String query, String type) {
        placesCoverLiveData.loadPlacesCover(
                googlePlaceRepository.searchPlaceByType(query, type, Locale.getDefault().getLanguage())
        );
    }
}
