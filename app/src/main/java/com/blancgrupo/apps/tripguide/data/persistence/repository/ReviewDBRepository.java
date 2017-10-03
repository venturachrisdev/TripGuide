package com.blancgrupo.apps.tripguide.data.persistence.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.data.persistence.ReviewsDao;
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

public class ReviewDBRepository implements ReviewsDao {

    PlacesDatabase database;
    CompositeDisposable disposable;

    public ReviewDBRepository(PlacesDatabase placesDatabase) {
        this.database = placesDatabase;
        disposable = new CompositeDisposable();
    }

    @Override
    public LiveData<List<ReviewModel>> getReviewsByProfile(final String profileId) {
        final MutableLiveData<List<ReviewModel>> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<List<ReviewModel>>>() {
                    @Override
                    public LiveData<List<ReviewModel>> call() throws Exception {
                        return database.reviewsDao().getReviewsByProfile(profileId);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<List<ReviewModel>>>() {
                            @Override
                            public void accept(@NonNull LiveData<List<ReviewModel>> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public LiveData<List<ReviewModel>> getReviewsByPlace(final String placeId) {
        final MutableLiveData<List<ReviewModel>> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<List<ReviewModel>>>() {
                    @Override
                    public LiveData<List<ReviewModel>> call() throws Exception {
                        return database.reviewsDao().getReviewsByPlace(placeId);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<List<ReviewModel>>>() {
                            @Override
                            public void accept(@NonNull LiveData<List<ReviewModel>> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public LiveData<ReviewModel> getReview(final String reviewId) {
        final MutableLiveData<ReviewModel> livedata = new MutableLiveData<>();
        disposable.add(
                Observable.fromCallable(new Callable<LiveData<ReviewModel>>() {
                    @Override
                    public LiveData<ReviewModel> call() throws Exception {
                        return database.reviewsDao().getReview(reviewId);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LiveData<ReviewModel>>() {
                            @Override
                            public void accept(@NonNull LiveData<ReviewModel> profile) throws Exception {
                                livedata.setValue(profile.getValue());
                            }
                        })
        );
        return livedata;
    }

    @Override
    public Long insertReview(final ReviewModel review) {
        disposable.add(
                Observable.fromCallable(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return database.reviewsDao().insertReview(review);
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
    public List<Long> insertReview(final List<ReviewModel> reviews) {
        disposable.add(
                Observable.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        database.reviewsDao().insertReview(reviews);
                        return new ArrayList<>();
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
    public int deleteReview(final ReviewModel review) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.reviewsDao().deleteReview(review);
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
    public int updateReview(final ReviewModel review) {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.reviewsDao().updateReview(review);
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
    public int deleteAllReviews() {
        disposable.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return database.reviewsDao().deleteAllReviews();
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
