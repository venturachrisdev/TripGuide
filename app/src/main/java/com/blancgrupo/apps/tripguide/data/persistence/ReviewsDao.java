package com.blancgrupo.apps.tripguide.data.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by venturachrisdev on 10/3/17.
 */

@Dao
public interface ReviewsDao {
    @Query("SELECT * FROM reviews WHERE profileId =  :profileId ORDER BY reviews.createdAt DESC")
    Single<List<ReviewModel>> getReviewsByProfile(String profileId);

    @Query("SELECT * FROM reviews WHERE reviews.placeId = :placeId ORDER BY reviews.createdAt DESC")
    Single<List<ReviewModel>> getReviewsByPlace(String placeId);

    @Query("SELECT * FROM reviews WHERE _id =  :reviewId LIMIT 1")
    Single<ReviewModel> getReview(String reviewId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertReview(ReviewModel review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertReview(List<ReviewModel> reviews);

    @Delete
    int deleteReview(ReviewModel review);

    @Update
    int updateReview(ReviewModel review);

    @Query("DELETE FROM reviews")
    int deleteAllReviews();
}
