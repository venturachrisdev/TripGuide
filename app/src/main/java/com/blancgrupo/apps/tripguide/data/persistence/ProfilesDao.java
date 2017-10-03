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

    @Query("UPDATE profiles SET tokenId = NULL WHERE tokenId IS NOT NULL")
    int logoutProfile();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertProfile(ProfileModel profile);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertProfiles(List<ProfileModel> profiles);

    @Delete
    int deleteProfile(ProfileModel profile);

    @Update
    int updateProfile(ProfileModel profile);

    @Query("DELETE FROM profiles")
    int deleteAllProfiles();
}
