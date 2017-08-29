package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.ToursLiveData;

/**
 * Created by root on 8/29/17.
 */

public class TourViewModel extends ViewModel {
    PlaceRepository placeRepository;
    ToursLiveData toursLiveData;

    public TourViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public LiveData<PlacesWrapper> getTours() {
        if (toursLiveData == null) {
            toursLiveData = new ToursLiveData();
        }
        loadTours();
        return toursLiveData;
    }

    public void loadTours() {
        if (toursLiveData != null) {
            toursLiveData.loadTours(placeRepository.getTours());
        }
    }
}
