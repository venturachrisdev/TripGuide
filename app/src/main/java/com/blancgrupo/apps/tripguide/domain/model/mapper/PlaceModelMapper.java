package com.blancgrupo.apps.tripguide.domain.model.mapper;

import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
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
        entity.setCity(place.getCity().getName());
        entity.setCityId(place.getCity().getId());
        entity.setCreatedAt(place.getCreatedAt());
        entity.setFavorite(place.isFavorite());
        entity.setName(place.getName());
        entity.setRating(place.getRating());
        entity.setUserHasReviewed(place.isUserHasReviewed());
        entity.setWebsite(place.getWebsite());
        entity.setWeekdays(place.getOpeningHours().getWeekdays().toString());
        entity.setLat(place.getLocation().getLat());
        entity.setLng(place.getLocation().getLng());
        entity.setPhotoUrl(ApiUtils.getPlacePhotoUrlWithoutKey(
                place.getPhoto().getReference(),
                place.getPhoto().getWidth()
        ));
        // photos
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
        entity.setWeekdays(place.getOpeningHours().getWeekdays().toString());
        entity.setLat(place.getLocation().getLat());
        entity.setLng(place.getLocation().getLng());
        entity.setPhotoUrl(ApiUtils.getPlacePhotoUrlWithoutKey(
                place.getPhoto().getReference(),
                place.getPhoto().getWidth()
        ));
        // photos
        entity.setPhoneNumber(place.getPhoneNumber());
        return entity;
    }
}
