package com.blancgrupo.apps.tripguide.domain.mapper;

import android.support.annotation.NonNull;

import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.OpeningHours;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceDetail;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 8/25/17.
 */

public class GooglePlaceMapper {

    public static PlaceCover transformCover(@NonNull PlaceDetail placeDetail) {
        PlaceCover cover = new PlaceCover();
        cover.setName(placeDetail.getName());
        cover.setAddress(placeDetail.getFormattedAddress() != null
            ? placeDetail.getFormattedAddress() : placeDetail.getVicinity());
        cover.setGoogleId(placeDetail.getPlaceId());
        cover.setPhoneNumber(placeDetail.getInternationalPhoneNumber() != null
            ? placeDetail.getInternationalPhoneNumber() : placeDetail.getFormattedPhoneNumber());
        cover.setLocation(new Location(placeDetail.getGeometry().getLocation().getLat(),
                placeDetail.getGeometry().getLocation().getLng()));
        cover.setType(placeDetail.getTypes().get(0));
        if (placeDetail.getPhotos() != null && placeDetail.getPhotos().size() > 0) {
            cover.setPhoto(new Photo(placeDetail.getPhotos().get(0).getPhotoReference(),
                    placeDetail.getPhotos().get(0).getWidth()));
        }
        if (placeDetail.getOpeningHours() != null
                && placeDetail.getOpeningHours().getWeekdayText() != null) {
            cover.setOpeningHours(new OpeningHours(placeDetail.getOpeningHours().getWeekdayText(),
                    placeDetail.getOpeningHours().getOpenNow()));
        }
        return cover;
    }

    public static PlaceCover transformCover(@NonNull PlaceSearch placeDetail) {
        PlaceCover place = new PlaceCover();
        place.setName(placeDetail.getName());
        place.setAddress(placeDetail.getFormattedAddress());
        place.setGoogleId(placeDetail.getPlaceId());
        place.setLocation(new Location(placeDetail.getGeometry().getLocation().getLat(),
                placeDetail.getGeometry().getLocation().getLng()));
        place.setType(placeDetail.getTypes().get(0));
        if (placeDetail.getPhotos() != null && placeDetail.getPhotos().size() > 0) {
            place.setPhoto(new Photo(placeDetail.getPhotos().get(0).getPhotoReference(),
                    placeDetail.getPhotos().get(0).getWidth()));
        }
        if (placeDetail.getOpeningHours() != null
                && placeDetail.getOpeningHours().getWeekdayText() != null) {
            place.setOpeningHours(new OpeningHours(placeDetail.getOpeningHours().getWeekdayText(),
                    placeDetail.getOpeningHours().getOpenNow()));
        }
        return place;
    }

    public static Place transformDetail(@NonNull PlaceDetail placeDetail) {
        Place place = new Place();
        place.setName(placeDetail.getName());
        place.setRating(placeDetail.getRating());
        place.setAddress(placeDetail.getFormattedAddress() != null
                ? placeDetail.getFormattedAddress() : placeDetail.getVicinity());
        place.setGoogleId(placeDetail.getPlaceId());
        place.setPhoneNumber(placeDetail.getInternationalPhoneNumber() != null
                ? placeDetail.getInternationalPhoneNumber() : placeDetail.getFormattedPhoneNumber());
        place.setLocation(new Location(placeDetail.getGeometry().getLocation().getLat(),
                placeDetail.getGeometry().getLocation().getLng()));
        place.setTypes(placeDetail.getTypes());
        if (placeDetail.getPhotos() != null && placeDetail.getPhotos().size() > 0) {
            place.setPhoto(new Photo(placeDetail.getPhotos().get(0).getPhotoReference(),
                    placeDetail.getPhotos().get(0).getWidth()));
            List<Photo> photos = new ArrayList<>();
            for (com.blancgrupo.apps.tripguide.data.entity.googleplace.Photo gPhoto
                    : placeDetail.getPhotos()) {
                photos.add(new Photo(gPhoto.getPhotoReference(), gPhoto.getWidth()));
            }
            place.setPhotos(photos);
        }
        if (placeDetail.getOpeningHours() != null
                && placeDetail.getOpeningHours().getWeekdayText() != null) {
            place.setOpeningHours(new OpeningHours(placeDetail.getOpeningHours().getWeekdayText(),
                    placeDetail.getOpeningHours().getOpenNow()));
        }
        return place;
    }

    public static List<Place> transformDetail(List<PlaceDetail> placesDetail) {
        if (placesDetail != null && placesDetail.size() > 0) {
            List<Place> places = new ArrayList<>();
            for (PlaceDetail detail : placesDetail) {
                places.add(transformDetail(detail));
            }
            return places;
        }
        return null;
    }

    public static List<PlaceCover> transformCover(List<PlaceDetail> placesDetail) {
        if (placesDetail != null && placesDetail.size() > 0) {
            List<PlaceCover> places = new ArrayList<>();
            for (PlaceDetail detail : placesDetail) {
                places.add(transformCover(detail));
            }
            return places;
        }
        return null;
    }

    public static List<PlaceCover> transformCoverFromSearch(List<PlaceSearch> placesDetail) {
        if (placesDetail != null && placesDetail.size() > 0) {
            List<PlaceCover> places = new ArrayList<>();
            for (PlaceSearch detail : placesDetail) {
                places.add(transformCover(detail));
            }
            return places;
        }
        return null;
    }
}
