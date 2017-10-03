package com.blancgrupo.apps.tripguide.data.persistence.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.data.persistence.ProfilesDao;
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

public class ProfileDBRepository implements ProfilesDao {
    PlacesDatabase database;
    CompositeDisposable disposable;

    public ProfileDBRepository(PlacesDatabase placesDatabase) {
        this.database = placesDatabase;
        disposable = new CompositeDisposable();
    }


    @Override
    public LiveData<ProfileWithReviews> getProfile(final String profileId) {
        final MutableLiveData<ProfileWithReviews> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<ProfileWithReviews>>() {
                    @Override
                    public LiveData<ProfileWithReviews> call() throws Exception {
                        return database.profilesDao().getProfile(profileId);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<ProfileWithReviews>>() {
                            @Override
                            public void accept(@NonNull LiveData<ProfileWithReviews> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public LiveData<ProfileWithReviews> getProfileByToken(final String apiToken) {
        final MutableLiveData<ProfileWithReviews> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<ProfileWithReviews>>() {
                    @Override
                    public LiveData<ProfileWithReviews> call() throws Exception {
                        return database.profilesDao().getProfileByToken(apiToken);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<ProfileWithReviews>>() {
                            @Override
                            public void accept(@NonNull LiveData<ProfileWithReviews> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public int logoutProfile() {
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

                            }
                        })
        );
        return 0;
    }

    @Override
    public Long insertProfile(final ProfileModel profile) {
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
        return null;
    }

    @Override
    public List<Long> insertProfiles(final List<ProfileModel> profiles) {
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

                            }
                        })
        );
        return null;
    }

    @Override
    public int deleteProfile(final ProfileModel profile) {
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

                            }
                        })
        );
        return 0;
    }

    @Override
    public int updateProfile(final ProfileModel profile) {
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

                            }
                        })
        );
        return 0;
    }

    @Override
    public int deleteAllProfiles() {
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

                            }
                        })
        );
        return 0;
    }

    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
