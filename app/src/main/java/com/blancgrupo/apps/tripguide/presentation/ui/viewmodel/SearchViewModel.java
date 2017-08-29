package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceSearchWrapper;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.mapper.EntityModelMapper;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata.PlacesCoverLiveData;

import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
        if (placesCoverLiveData != null) {
            placesCoverLiveData.loadPlacesCover(
                    googlePlaceRepository.searchPlaceByType(query, type, Locale.getDefault().getLanguage())
            );
        }
    }
}
