package com.blancgrupo.apps.tripguide.data.persistence.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDao;
import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceWithReviews;

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

public class PlaceDBRepository implements PlacesDao {

    PlacesDatabase database;
    CompositeDisposable disposable;

    public PlaceDBRepository(PlacesDatabase placesDatabase) {
        this.database = placesDatabase;
        disposable = new CompositeDisposable();
    }

    @Override
    public LiveData<List<PlaceWithReviews>> getPlaces() {
        final MutableLiveData<List<PlaceWithReviews>> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<List<PlaceWithReviews>>>() {
                    @Override
                    public LiveData<List<PlaceWithReviews>> call() throws Exception {
                        return database.placesDao().getPlaces();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<List<PlaceWithReviews>>>() {
                            @Override
                            public void accept(@NonNull LiveData<List<PlaceWithReviews>> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public LiveData<List<PlaceWithReviews>> getPlacesByType(final String placeType, final String cityId) {
        final MutableLiveData<List<PlaceWithReviews>> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<List<PlaceWithReviews>>>() {
                    @Override
                    public LiveData<List<PlaceWithReviews>> call() throws Exception {
                        return database.placesDao().getPlacesByType(placeType, cityId);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<List<PlaceWithReviews>>>() {
                            @Override
                            public void accept(@NonNull LiveData<List<PlaceWithReviews>> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public LiveData<PlaceWithReviews> getPlaceById(final String placeId) {
        final MutableLiveData<PlaceWithReviews> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<PlaceWithReviews>>() {
                    @Override
                    public LiveData<PlaceWithReviews> call() throws Exception {
                        return database.placesDao().getPlaceById(placeId);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<PlaceWithReviews>>() {
                            @Override
                            public void accept(@NonNull LiveData<PlaceWithReviews> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public LiveData<List<PlaceModel>> getPlacesFavorite() {
        final MutableLiveData<List<PlaceModel>> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<List<PlaceModel>>>() {
                    @Override
                    public LiveData<List<PlaceModel>> call() throws Exception {
                        return database.placesDao().getPlacesFavorite();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<List<PlaceModel>>>() {
                            @Override
                            public void accept(@NonNull LiveData<List<PlaceModel>> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public Long insertPlace(final PlaceModel place) {
        disposable.add(
                Observable.fromCallable(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return database.placesDao().insertPlace(place);
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
    public List<Long> insertPlace(final List<PlaceModel> places) {
        disposable.add(
                Observable.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        return database.placesDao().insertPlace(places);
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
    public int deletePlace(final PlaceModel place) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.placesDao().deletePlace(place);
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
    public int deleteAllPlaces() {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.placesDao().deleteAllPlaces();
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
    public int updatePlace(final PlaceModel place) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.placesDao().updatePlace(place);
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
