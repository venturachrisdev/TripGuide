package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.CountriesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.CountryWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class CountryLiveData extends LiveData<CountryWrapper> {
    Disposable disposable;

    public void getCountry(Observable<CountryWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CountryWrapper>() {
                    @Override
                    public void accept(CountryWrapper countriesWrapper) throws Exception {
                        setValue(countriesWrapper);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        setValue(new CountryWrapper());
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
