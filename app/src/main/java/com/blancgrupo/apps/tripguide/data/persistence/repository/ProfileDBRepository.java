package com.blancgrupo.apps.tripguide.data.persistence.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.domain.model.ProfileModel;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by venturachrisdev on 10/3/17.
 */

public class ProfileDBRepository {
    PlacesDatabase database;
    CompositeDisposable disposable;

    public ProfileDBRepository(PlacesDatabase placesDatabase) {
        this.database = placesDatabase;
        disposable = new CompositeDisposable();
    }


    public LiveData<ProfileWithReviews> getProfile(final String profileId) {
        final MutableLiveData<ProfileWithReviews> livedata = new MutableLiveData<>();
        disposable.add(
                database.profilesDao().getProfile(profileId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ProfileWithReviews>() {
                            @Override
                            public void accept(@NonNull ProfileWithReviews profile) throws Exception {
                                Log.d("PlaceDatabase", "getProfile returned");
                                livedata.setValue(profile);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getProfile returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    public LiveData<ProfileWithReviews> getProfileByToken(final String apiToken) {
        final MutableLiveData<ProfileWithReviews> livedata = new MutableLiveData<>();
        disposable.add(
                database.profilesDao().getProfileByToken(apiToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ProfileWithReviews>() {
                            @Override
                            public void accept(@NonNull ProfileWithReviews profile) throws Exception {
                                Log.d("PlaceDatabase", "getProfileByToken returned");
                                livedata.setValue(profile);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getProfileByToken returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    public void logoutProfile() {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.profilesDao().logoutProfile();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "logoutProfile returned");
                            }
                        })
        );
    }

    public void insertProfile(final ProfileModel profile) {
        disposable.add(
                Observable.fromCallable(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return database.profilesDao().insertProfile(profile);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aVoid) throws Exception {

                    }
                })
        );
    }

    public void insertProfiles(final List<ProfileModel> profiles) {
        disposable.add(
                Observable.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        return database.profilesDao().insertProfiles(profiles);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Long>>() {
                            @Override
                            public void accept(@NonNull List<Long> aVoid) throws Exception {
                                Log.d("PlaceDatabase", "insertProfiles returned");
                            }
                        })
        );
    }

    public void deleteProfile(final ProfileModel profile) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.profilesDao().deleteProfile(profile);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "deleteProfile returned");
                            }
                        })
        );
    }

    public void updateProfile(final ProfileModel profile) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.profilesDao().updateProfile(profile);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "updateProfile returned");
                            }
                        })
        );
    }

    public void deleteAllProfiles() {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.profilesDao().deleteAllProfiles();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "deleteProfiles returned");
                            }
                        })
        );
    }

    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
