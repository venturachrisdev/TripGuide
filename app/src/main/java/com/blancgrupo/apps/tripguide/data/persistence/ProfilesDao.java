package com.blancgrupo.apps.tripguide.data.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.blancgrupo.apps.tripguide.domain.model.ProfileModel;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;

import java.util.List;

/**
 * Created by venturachrisdev on 10/3/17.
 */

@Dao
public interface ProfilesDao {
    @Query("SELECT * from profiles WHERE _id = :profileId")
    LiveData<ProfileWithReviews> getProfile(String profileId);

    @Query("SELECT * from profiles WHERE tokenId = :apiToken")
    LiveData<ProfileWithReviews> getProfileByToken(String apiToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfile(ProfileModel profile);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfiles(List<ProfileModel> profiles);

    @Delete
    void deleteProfile(ProfileModel profile);

    @Update
    void updateProfile(ProfileModel profile);

    @Query("DELETE FROM profiles")
    void deleteAllProfiles();
}
