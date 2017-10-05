package com.blancgrupo.apps.tripguide.data.persistence.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.domain.model.PhotoModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceWithReviews;

import java.util.ArrayList;
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

public class PlaceDBRepository {

    PlacesDatabase database;
    CompositeDisposable disposable;

    public PlaceDBRepository(PlacesDatabase placesDatabase) {
        this.database = placesDatabase;
        disposable = new CompositeDisposable();
    }

    public LiveData<List<PlaceWithReviews>> getPlaces() {
        final MutableLiveData<List<PlaceWithReviews>> livedata = new MutableLiveData<>();
        disposable.add(
                database.placesDao().getPlaces()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<PlaceWithReviews>>() {
                            @Override
                            public void accept(@NonNull List<PlaceWithReviews> places) throws Exception {
                                Log.d("PlaceDatabase", "getPlaces returned");
                                livedata.setValue(places);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getPlaces returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    public LiveData<List<PlaceWithReviews>> getPlacesByType(final String placeType, final String cityId) {
        final MutableLiveData<List<PlaceWithReviews>> livedata = new MutableLiveData<>();
        disposable.add(
                database.placesDao().getPlacesByType(placeType, cityId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<PlaceWithReviews>>() {
                            @Override
                            public void accept(@NonNull List<PlaceWithReviews> places) throws Exception {
                                Log.d("PlaceDatabase", "getPlacesByType returned");
                                livedata.setValue(places);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getPlacesByType returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    public LiveData<PlaceWithReviews> getPlaceById(final String placeId) {
        final MutableLiveData<PlaceWithReviews> livedata = new MutableLiveData<>();
        disposable.add(
                database.placesDao().getPlaceById(placeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<PlaceWithReviews>() {
                            @Override
                            public void accept(@NonNull PlaceWithReviews place) throws Exception {
                                Log.d("PlaceDatabase", "getPlaceById returned");
                                livedata.setValue(place);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getPlaceById returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    public LiveData<List<PlaceModel>> getPlacesFavorite() {
        final MutableLiveData<List<PlaceModel>> livedata = new MutableLiveData<>();
        disposable.add(database.placesDao().getPlacesFavorite()
                .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<PlaceModel>>() {
                            @Override
                            public void accept(@NonNull List<PlaceModel> places) throws Exception {
                                Log.d("PlaceDatabase", "getPlacesFavorite returned");
                                livedata.setValue(places);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                Log.d("PlaceDatabase", "getPlacesFavorite returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    public void insertPlace(final PlaceModel place, final List<PhotoModel> photos) {
        disposable.add(
                Observable.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        database.placesDao().insertPlace(place);
                        if (photos != null) {
                            return database.photoDao().insertPhotos(photos);
                        }
                        return new ArrayList<>();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Long>>() {
                            @Override
                            public void accept(@NonNull List<Long> aVoid) throws Exception {

                            }
                        })
        );
    }

    public LiveData<List<Long>> insertPlace(final List<PlaceModel> places) {
        final MutableLiveData<List<Long>> liveData = new MutableLiveData<>();
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
                                liveData.setValue(aVoid);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                liveData.setValue(null);
                            }
                        })
        );
        return liveData;
    }

    public LiveData<List<Long>> insertPlaceFavorites(final List<PlaceModel> places) {
        for (int i = 0; i < places.size() ; i++) {
            places.get(i).setFavorite(true);
        }
        final MutableLiveData<List<Long>> liveData = new MutableLiveData<>();
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
                                liveData.setValue(aVoid);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                liveData.setValue(null);
                            }
                        })
        );
        return liveData;
    }

    public LiveData<Integer> emptyFavorites() {
        final MutableLiveData<Integer> liveData = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.placesDao().emptyFavorites();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                liveData.setValue(aVoid);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                liveData.setValue(null);
                            }
                        })
        );
        return liveData;
    }

    public void deletePlace(final PlaceModel place) {
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
                                Log.d("PlaceDatabase", "deletePlace returned");
                            }
                        })
        );
    }

    public void deleteAllPlaces() {
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
                                Log.d("PlaceDatabase", "deleteAllPlaces returned");
                            }
                        })
        );
    }

    public void updatePlace(final PlaceModel place) {
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
                                Log.d("PlaceDatabase", "updatePlace returned");
                            }
                        })
        );
    }

    public void setFavorite(final String placeId, final boolean isFavorite) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.placesDao().setFavorite(placeId, isFavorite);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "setFavorite returned");
                            }
                        })
        );
    }


    public void setReviewed(final String placeId, final boolean isReviewed) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.placesDao().setFavorite(placeId, isReviewed);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "setReviewed returned");
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
