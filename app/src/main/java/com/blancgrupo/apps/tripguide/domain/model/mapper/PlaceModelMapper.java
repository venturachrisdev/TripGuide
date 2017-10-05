package com.blancgrupo.apps.tripguide.domain.model.mapper;

import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.domain.model.PhotoModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceWithReviews;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venturachrisdev on 10/3/17.
 */

public class PlaceModelMapper {

    public static PlaceWithReviews transform(Place place) {
        if (place == null) {
            return null;
        }
        PlaceWithReviews entity = new PlaceWithReviews();
        entity.setPlace(transformPlace(place));
        entity.setReviews(transformAllReviews(place.getReviews()));
        entity.setPhotos(transformPhotos(place.getPhotos(), place.getId()));
        return entity;
    }

    public static List<PlaceModel> transformAll(List<Place> places) {
        List<PlaceModel> entities = new ArrayList<>();
        for (Place place: places) {
            entities.add(transformPlace(place));
        }
        return entities;
    }

    public static List<PlaceModel> transformAllCover(List<PlaceCover> places) {
        List<PlaceModel> entities = new ArrayList<>();
        for (PlaceCover place: places) {
            entities.add(transformPlace(place));
        }
        return entities;
    }

    public static List<ReviewModel> transformAllReviews(List<Review> reviews) {
        if (reviews == null || reviews.size() < 1) {
            return null;
        }
        List<ReviewModel> entities = new ArrayList<>();
        for (Review review : reviews) {
            entities.add(transformReview(review));
        }
        return entities;
    }

    public static ReviewModel transformReview(Review review) {
        if (review == null) {
            return null;
        }
        ReviewModel entity = new ReviewModel(
                review.get_id(),
                review.getRating(),
                review.getMessage(),
                review.getCreatedAt(),
                review.getPhoto(),
                review.getPlace().get_id(),
                review.getProfile().get_id()
        );
        entity.setProfileName(review.getProfile().getName());
        entity.setProfilePhotoUrl(review.getProfile().getPhotoUrl());
        entity.setPlaceName(review.getPlace().getName());
        entity.setPlaceCity(review.getPlace().getAddress());
        return entity;
    }

    public static PlaceModel transformPlace(Place place) {
        if (place == null) {
            return null;
        }
        PlaceModel entity = new PlaceModel();
        // pass properties
        entity.set_id(place.getId());
        entity.setGoogleId(place.getGoogleId());
        entity.setAddress(place.getAddress());
        entity.setType(place.getTypes().get(0));
        if (place.getCity() != null) {
            entity.setCity(place.getCity().getName());
            entity.setCityId(place.getCity().getId());
        }
        entity.setCreatedAt(place.getCreatedAt());
        entity.setFavorite(place.isFavorite());
        entity.setName(place.getName());
        entity.setRating(place.getRating());
        entity.setUserHasReviewed(place.isUserHasReviewed());
        entity.setWebsite(place.getWebsite());
        if (place.getOpeningHours() != null) {
            entity.setWeekdays(place.getOpeningHours().getWeekdays().toString());
        }
        if (place.getLocation() != null) {
            entity.setLat(place.getLocation().getLat());
            entity.setLng(place.getLocation().getLng());
        }
        entity.setPhotoUrl(ApiUtils.getPlacePhotoUrlWithoutKey(
                place.getPhoto().getReference(),
                place.getPhoto().getWidth()
        ));
        entity.setPhoneNumber(place.getPhoneNumber());
        return entity;
    }

    public static PlaceModel transformPlace(PlaceCover place) {
        if (place == null) {
            return null;
        }
        PlaceModel entity = new PlaceModel();
        // pass properties
        entity.set_id(place.getId());
        entity.setGoogleId(place.getGoogleId());
        entity.setAddress(place.getAddress());
        entity.setType(place.getType());
        entity.setCity(place.getCity());
        entity.setCreatedAt(place.getCreatedAt());
        entity.setName(place.getName());
        entity.setRating(place.getRating());
        if (place.getOpeningHours() != null) {
            entity.setWeekdays(place.getOpeningHours().getWeekdays().toString());
        }
        if (place.getLocation() != null) {
            entity.setLat(place.getLocation().getLat());
            entity.setLng(place.getLocation().getLng());
            if (place.getPhoto() != null) {
                entity.setPhotoUrl(ApiUtils.getPlacePhotoUrlWithoutKey(
                        place.getPhoto().getReference(),
                        place.getPhoto().getWidth()
                ));
            }
        }
        return entity;
    }


    public static List<PhotoModel> transformPhotos(List<Photo> photos, String placeId) {
        if (photos == null) {
            return null;
        }
        List<PhotoModel> models = new ArrayList<>();
        for (Photo photo : photos) {
            models.add(transformPhoto(photo, placeId));
        }
        return models;
    }

    private static PhotoModel transformPhoto(Photo photo, String placeId) {
        if (photo == null) {
            return null;
        }
        PhotoModel model = new PhotoModel();
        model.setPlaceId(placeId);
        model.setReference(photo.getReference());
        model.setWidth(photo.getWidth());
        return model;
    }

    public static List<Photo> rollbackPhotos(List<PhotoModel> photos) {
        if (photos == null) {
            return null;
        }
        List<Photo> models = new ArrayList<>();
        for (PhotoModel photo : photos) {
            models.add(rollbackPhoto(photo));
        }
        return models;
    }

    private static Photo rollbackPhoto(PhotoModel photo) {
        if (photo == null) {
            return null;
        }
        return new Photo(photo.getReference(), photo.getWidth());
    }

    public static ReviewModel transformReview(ReviewResponseWrapper.ReviewResponse review) {
        if (review == null) {
            return null;
        }
        ReviewModel entity = new ReviewModel(
                review.get_id(),
                review.getRating(),
                review.getMessage(),
                review.getCreatedAt(),
                null,
                review.getPlace(),
                review.getProfile()
        );
        return entity;
    }
}
