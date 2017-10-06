package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.mapper.PlaceModelMapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by root on 8/28/17.
 */

public class PlacesLiveData extends LiveData<List<PlaceModel>> {
    Disposable disposable;

    public void loadPlaces(Observable<PlacesWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .map(new Function<PlacesWrapper, List<PlaceModel>>() {
                    @Override
                    public List<PlaceModel> apply(@NonNull PlacesWrapper placesWrapper) throws Exception {
                        return PlaceModelMapper.transformAll(placesWrapper.getPlaces());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PlaceModel>>() {
                    @Override
                    public void accept(@NonNull List<PlaceModel> places) throws Exception {
                        setValue(places);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        setValue(null);
                    }
                });
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
