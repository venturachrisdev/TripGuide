package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.domain.model.PlaceWithReviews;
import com.blancgrupo.apps.tripguide.domain.model.mapper.PlaceModelMapper;

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

public class PlaceLiveData extends LiveData<PlaceWithReviews> {
    Disposable disposable;

    public void loadSinglePlace(Observable<PlaceWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .map(new Function<PlaceWrapper, PlaceWithReviews>() {
                    @Override
                    public PlaceWithReviews apply(@NonNull PlaceWrapper placeWrapper) throws Exception {
                        return PlaceModelMapper.transform(placeWrapper.getPlace());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlaceWithReviews>() {
                    @Override
                    public void accept(@NonNull PlaceWithReviews placeWithReviews) throws Exception {
                        setValue(placeWithReviews);
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
