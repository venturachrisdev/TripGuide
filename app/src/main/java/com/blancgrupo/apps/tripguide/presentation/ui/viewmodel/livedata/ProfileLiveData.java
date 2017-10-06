package com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.livedata;

import android.arch.lifecycle.LiveData;

import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;
import com.blancgrupo.apps.tripguide.domain.model.mapper.ProfileModelMapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by venturachrisdev on 9/21/17.
 */

public class ProfileLiveData extends LiveData<ProfileWithReviews> {
    Disposable disposable;

    public void loadProfile(Observable<ProfileWrapper> observable) {
        disposable = observable
                .subscribeOn(Schedulers.io())
                .map(new Function<ProfileWrapper, ProfileWithReviews>() {
                    @Override
                    public ProfileWithReviews apply(@NonNull ProfileWrapper profileWrapper) throws Exception {
                        return ProfileModelMapper.transform(profileWrapper.getProfile(), profileWrapper.getToken());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProfileWithReviews>() {
                    @Override
                    public void accept(@NonNull ProfileWithReviews profile) throws Exception {
                        setValue(profile);
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
