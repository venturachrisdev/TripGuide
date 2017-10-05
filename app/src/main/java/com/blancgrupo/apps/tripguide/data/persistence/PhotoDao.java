package com.blancgrupo.apps.tripguide.data.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.blancgrupo.apps.tripguide.domain.model.PhotoModel;

import java.util.List;

/**
 * Created by venturachrisdev on 10/4/17.
 */

@Dao
public interface PhotoDao {

    @Insert
    List<Long> insertPhotos(List<PhotoModel> photos);

    @Insert
    Long insertPhoto(PhotoModel photos);

    @Delete
    int deletePhoto(PhotoModel photo);

    @Update
    int updatePhoto(PhotoModel photo);
}
