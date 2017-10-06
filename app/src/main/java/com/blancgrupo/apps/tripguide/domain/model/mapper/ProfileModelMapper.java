package com.blancgrupo.apps.tripguide.domain.model.mapper;

import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.domain.model.ProfileModel;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;

/**
 * Created by venturachrisdev on 10/3/17.
 */

public class ProfileModelMapper {

    public static ProfileWithReviews transform(Profile profile, String token) {
        if (profile == null) {
            return null;
        }
        ProfileWithReviews entity = new ProfileWithReviews();
        entity.setProfile(transformProfile(profile));
        entity.setReviews(PlaceModelMapper.transformAllReviews(profile.getReviews()));
        entity.setApiToken(token);
        return entity;
    }

    public static ProfileModel transformProfile(Profile profile) {
        if (profile == null) {
            return null;
        }
        ProfileModel entity = new ProfileModel();
        entity.setName(profile.getName());
        entity.set_id(profile.get_id());
        entity.setCreatedAt(profile.getCreatedAt());
        entity.setEmail(profile.getEmail());
        entity.setExperience(profile.getExperience());
        entity.setTokenId(profile.getTokenId());
        entity.setPhotoUrl(profile.getPhotoUrl());
        return entity;
    }
}
