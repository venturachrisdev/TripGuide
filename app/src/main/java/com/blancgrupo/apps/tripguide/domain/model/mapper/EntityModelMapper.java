package com.blancgrupo.apps.tripguide.domain.model.mapper;

import android.support.annotation.NonNull;

import com.blancgrupo.apps.tripguide.data.entity.googleplace.AddressComponent;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.Location;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.OpeningHours;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.Photo;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceDetail;
import com.blancgrupo.apps.tripguide.data.entity.googleplace.PlaceSearch;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 8/15/17.
 */

public class EntityModelMapper {
    public static final String PHOTO_PLACE_API_URL = "https://maps.googleapis.com/maps/api/place/photo";


    /* From PlaceDetail to Model */
    public static PlaceModel transform(@NonNull PlaceDetail placeDetail) {
        PlaceModel model = new PlaceModel();
        model.setPlaceId(placeDetail.getPlaceId());
        model.setName(placeDetail.getName());
        model.setAddress(placeDetail.getFormattedAddress() != null
                ? placeDetail.getFormattedAddress() : placeDetail.getVicinity());
        Location location = placeDetail.getGeometry().getLocation();
        model.setLat(location.getLat());
        model.setLng(location.getLng());
        model.setPhoneNumber(placeDetail.getInternationalPhoneNumber() != null
                ? placeDetail.getInternationalPhoneNumber() : placeDetail.getFormattedPhoneNumber());
        model.setWebsite(placeDetail.getWebsite());
        if (placeDetail.getTypes() != null && placeDetail.getTypes().size() > 0) {
            model.setType(placeDetail.getTypes().get(0));
        }
        OpeningHours openingHours = placeDetail.getOpeningHours();
        if (openingHours != null) {
            model.setOpenNow(openingHours.getOpenNow());
            model.setWeekdays(openingHours.getWeekdayText().toString());
        }
        List<Photo> photos = placeDetail.getPhotos();
        if (photos != null && photos.size() > 0) {
            Photo photo = photos.get(0);
            String url = PHOTO_PLACE_API_URL
                    + "?maxwidth=" + photo.getWidth()
                    + "&photoreference=" + photo.getPhotoReference()
                    + "&key=";
            //TODO: Add api key in future calls
            model.setPhotoUrl(url);
        }
        //TODO: Set distance in future calls
        List<AddressComponent> addressComponent = placeDetail.getAddressComponents();
        if (addressComponent != null && addressComponent.size() > 0) {
            AddressComponent component = addressComponent.get(addressComponent.size() - 1);
            model.setCity(component.getLongName() != null
                ? component.getLongName() : component.getShortName());
        }

        return model;
    }

    public static List<PlaceModel> transformDetailAll(@NonNull List<PlaceDetail> placesDetail) {
        List<PlaceModel> models = new ArrayList<>();
        for (PlaceDetail place : placesDetail) {
            models.add(transform(place));
        }
        return models;
    }


    /* From PlaceSearch (less features) to Model */
    public static PlaceModel transform(@NonNull PlaceSearch placeSearch) {
        PlaceModel model = new PlaceModel();
        model.setPlaceId(placeSearch.getPlaceId());
        model.setName(placeSearch.getName());
        model.setAddress(placeSearch.getFormattedAddress());
        Location location = placeSearch.getGeometry().getLocation();
        model.setLat(location.getLat());
        model.setLng(location.getLng());
        if (placeSearch.getTypes() != null && placeSearch.getTypes().size() > 0) {
            model.setType(placeSearch.getTypes().get(0));
        }
        OpeningHours openingHours = placeSearch.getOpeningHours();
        if (openingHours != null) {
            model.setOpenNow(openingHours.getOpenNow());
            model.setWeekdays(openingHours.getWeekdayText().toString());
        }
        List<Photo> photos = placeSearch.getPhotos();
        if (photos != null && photos.size() > 0) {
            Photo photo = photos.get(0);
            String url = PHOTO_PLACE_API_URL
                    + "?maxwidth=" + photo.getWidth()
                    + "&photoreference=" + photo.getPhotoReference()
                    + "&key=";
            //TODO: Add api key in future calls
            model.setPhotoUrl(url);
        }
        //TODO: Set distance in future calls

        return model;
    }

    public static List<PlaceModel> transformSearchAll(@NonNull List<PlaceSearch> placesSearch) {
        List<PlaceModel> models = new ArrayList<>();
        for (PlaceSearch place : placesSearch) {
            models.add(transform(place));
        }
        return models;
    }
}
