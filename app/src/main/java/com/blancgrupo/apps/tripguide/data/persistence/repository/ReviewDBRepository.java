package com.blancgrupo.apps.tripguide.data.persistence.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;

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

public class ReviewDBRepository {

    PlacesDatabase database;
    CompositeDisposable disposable;

    public ReviewDBRepository(PlacesDatabase placesDatabase) {
        this.database = placesDatabase;
        disposable = new CompositeDisposable();
    }

    
    public LiveData<List<ReviewModel>> getReviewsByProfile(final String profileId) {
        final MutableLiveData<List<ReviewModel>> livedata = new MutableLiveData<>();
        disposable.add(
                database.reviewsDao().getReviewsByProfile(profileId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<ReviewModel>>() {
                            
                            public void accept(@NonNull List<ReviewModel> reviews) throws Exception {
                                Log.d("PlaceDatabase", "getReviewsByProfile returned");
                                livedata.setValue(reviews);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getReviewsByProfile returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    
    public LiveData<List<ReviewModel>> getReviewsByPlace(final String placeId) {
        final MutableLiveData<List<ReviewModel>> livedata = new MutableLiveData<>();
        disposable.add(
                database.reviewsDao().getReviewsByPlace(placeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<ReviewModel>>() {
                            
                            public void accept(@NonNull List<ReviewModel> reviews) throws Exception {
                                Log.d("PlaceDatabase", "getReviewsByPlace returned");
                                livedata.setValue(reviews);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getReviewsByPlace returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    
    public LiveData<ReviewModel> getReview(final String reviewId) {
        final MutableLiveData<ReviewModel> livedata = new MutableLiveData<>();
        disposable.add(
                database.reviewsDao().getReview(reviewId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ReviewModel>() {
                            
                            public void accept(@NonNull ReviewModel review) throws Exception {
                                Log.d("PlaceDatabase", "getReview returned");
                                livedata.setValue(review);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("PlaceDatabase", "getReview returned null");
                                livedata.setValue(null);
                            }
                        })
        );
        return livedata;
    }

    
    public void insertReview(final ReviewModel review) {
        disposable.add(
                Observable.fromCallable(new Callable<Long>() {
                    
                    public Long call() throws Exception {
                        return database.reviewsDao().insertReview(review);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            
                            public void accept(@NonNull Long aVoid) throws Exception {
                                Log.d("PlaceDatabase", "insertReview returned");
                            }
                        })
        );
    }

    
    public void insertReview(final List<ReviewModel> reviews) {
        disposable.add(
                Observable.fromCallable(new Callable<List<Long>>() {
                    
                    public List<Long> call() throws Exception {
                        database.reviewsDao().insertReview(reviews);
                        return new ArrayList<>();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Long>>() {
                            
                            public void accept(@NonNull List<Long> aVoid) throws Exception {
                                Log.d("PlaceDatabase", "insertReview returned");
                            }
                        })
        );
    }

    
    public void deleteReview(final ReviewModel review) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    
                    public Integer call() throws Exception {
                        return database.reviewsDao().deleteReview(review);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "deleteReview returned");
                            }
                        })
        );
    }

    
    public void updateReview(final ReviewModel review) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    
                    public Integer call() throws Exception {
                        return database.reviewsDao().updateReview(review);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "updateReview returned");
                            }
                        })
        );
    }

    
    public void deleteAllReviews() {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    
                    public Integer call() throws Exception {
                        return database.reviewsDao().deleteAllReviews();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            
                            public void accept(@NonNull Integer aVoid) throws Exception {
                                Log.d("PlaceDatabase", "deleteAllReviews returned");
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
