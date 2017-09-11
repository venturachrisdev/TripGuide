package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.entity.api.ParentTourWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Tour;
import com.blancgrupo.apps.tripguide.data.entity.api.TourWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.domain.repository.TourRepository;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.ParentTourLiveData;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.SingleTourLiveData;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.ToursLiveData;

/**
 * Created by root on 8/29/17.
 */

public class TourViewModel extends ViewModel {
    TourRepository tourRepository;
    ToursLiveData toursLiveData;
    SingleTourLiveData singleTourLiveData;
    ParentTourLiveData parentTourLiveData;

    public TourViewModel(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
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
            toursLiveData.loadTours(tourRepository.getTours());
        }
    }

    public LiveData<TourWrapper> getSingleTour(String tourId) {
        if (toursLiveData == null) {
            singleTourLiveData = new SingleTourLiveData();
        }
        loadSingleTour(tourId);
        return singleTourLiveData;
    }

    public void loadSingleTour(String tourId) {
        if (toursLiveData != null) {
            singleTourLiveData.loadSingleTour(tourRepository.getSingleTour(tourId));
        }
    }

    public LiveData<ParentTourWrapper> getParentTour(String placeId) {
        if (parentTourLiveData == null) {
            parentTourLiveData = new ParentTourLiveData();
        }
        loadParentTour(placeId);
        return parentTourLiveData;
    }

    public void loadParentTour(String placeId) {
        if (parentTourLiveData != null) {
            parentTourLiveData.loadSingleTour(tourRepository.getSinglePlaceTour(placeId));
        }
    }
}
