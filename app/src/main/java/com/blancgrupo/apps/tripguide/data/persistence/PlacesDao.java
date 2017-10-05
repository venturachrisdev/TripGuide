package com.blancgrupo.apps.tripguide.data.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceWithReviews;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by root on 8/15/17.
 */

@Dao
public interface PlacesDao {

    @Query("SELECT * FROM places ORDER BY createdAt DESC")
    Single<List<PlaceWithReviews>> getPlaces();

    @Query("SELECT * FROM places WHERE type = :placeType AND cityId = :cityId ORDER BY createdAt DESC")
    Single<List<PlaceWithReviews>> getPlacesByType(String placeType, String cityId);

    @Query("SELECT * FROM places WHERE _id = :placeId LIMIT 1")
    Single<PlaceWithReviews> getPlaceById(String placeId);

    @Query("SELECT * FROM places WHERE isFavorite = 1 ORDER BY name ASC")
    Single<List<PlaceModel>> getPlacesFavorite();

    @Query("UPDATE places SET isFavorite = 0")
    int emptyFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPlace(PlaceModel place);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertPlace(List<PlaceModel> places);

    @Delete
    int deletePlace(PlaceModel place);

    @Query("DELETE FROM places")
    int deleteAllPlaces();

    @Update
    int updatePlace(PlaceModel place);

    @Query("UPDATE places SET isFavorite = :isFavorite WHERE _id = :placeId")
    int setFavorite(String placeId, boolean isFavorite);

    @Query("UPDATE places SET userHasReviewed = :isReviewed WHERE _id = :placeId")
    int setReviewed(String placeId, boolean isReviewed);
}

