package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by root on 8/28/17.
 */

public class CitiesLiveData extends LiveData<CitiesWrapper> {
    Disposable disposable;


    public void loadCities(Observable<CitiesWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CitiesWrapper>() {
                    @Override
                    public void accept(@NonNull CitiesWrapper citiesWrapper) throws Exception {
                        setValue(citiesWrapper);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        setValue(new CitiesWrapper(null, throwable.getMessage()));
                    }
                });
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
