package com.blancgrupo.apps.tripguide.data.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.blancgrupo.apps.tripguide.domain.model.PhotoModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.ProfileModel;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;

/**
 * Created by root on 8/15/17.
 */

@Database(exportSchema = false, entities = {
        PlaceModel.class, ProfileModel.class, ReviewModel.class, PhotoModel.class
    }, version = 1)
public abstract class PlacesDatabase  extends RoomDatabase {
    private static PlacesDatabase INSTANCE;
    public abstract PlacesDao placesDao();
    public abstract ProfilesDao profilesDao();
    public abstract ReviewsDao reviewsDao();
    public abstract PhotoDao photoDao();

    public static PlacesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlacesDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PlacesDatabase.class, "places.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}
