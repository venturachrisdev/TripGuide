package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by root on 8/28/17.
 */

public class PlaceLiveData extends LiveData<PlaceWrapper> {
    Disposable disposable;

    public void loadSinglePlace(Observable<PlaceWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlaceWrapper>() {
                    @Override
                    public void accept(@NonNull PlaceWrapper placeWrapper) throws Exception {
                        setValue(placeWrapper);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        setValue(new PlaceWrapper(null, throwable.getMessage()));
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
