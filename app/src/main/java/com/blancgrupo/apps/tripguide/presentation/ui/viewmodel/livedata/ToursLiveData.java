package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by root on 8/29/17.
 */

public class ToursLiveData extends LiveData<PlacesWrapper> {
    Disposable disposable;

    public void loadTours(Observable<PlacesWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlacesWrapper>() {
                    @Override
                    public void accept(@NonNull PlacesWrapper placesWrapper) throws Exception {
                        setValue(placesWrapper);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        setValue(new PlacesWrapper(null, throwable.getMessage()));
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
