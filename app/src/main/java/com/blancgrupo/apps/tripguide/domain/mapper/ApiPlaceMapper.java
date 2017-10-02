package com.blancgrupo.apps.tripguide.domain.mapper;

import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venturachrisdev on 9/29/17.
 */

public class ApiPlaceMapper {

    public static PlaceCover PlaceToCoverTransform(Place place) {
        if (place != null) {
            PlaceCover cover = new PlaceCover(
                    place.getId(), place.getTypes().get(0), place.getAddress(),
                    place.getGoogleId(), place.getCity().getName(), place.getName(),
                    place.getV(), place.getCreatedAt(), place.getOpeningHours(), place.getPhoto(),
                    place.getLocation(), place.getRating(), place.getPhoneNumber()
            );
            return cover;
        }
        return null;
    }

    public static List<PlaceCover> PlaceToCoverTransform(List<Place> places) {
        if (places != null && places.size() > 0) {
            List<PlaceCover> covers = new ArrayList<>();
            for (Place place: places) {
                covers.add(PlaceToCoverTransform(place));
            }
            return covers;
        }
        return null;
    }
}
