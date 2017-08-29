package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by root on 8/28/17.
 */

public class PlacesCoverLiveData extends LiveData<PlacesCoverWrapper> {
    Disposable disposable;

    public void loadPlacesCover(Observable<PlacesCoverWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlacesCoverWrapper>() {
                    @Override
                    public void accept(@NonNull PlacesCoverWrapper placesCoverWrapper) throws Exception {
                        setValue(placesCoverWrapper);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        setValue(new PlacesCoverWrapper(null, throwable.getMessage()));
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
