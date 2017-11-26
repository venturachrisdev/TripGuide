package com.blancgrupo.apps.tripguide.domain.repository;

import com.blancgrupo.apps.tripguide.data.entity.api.RegionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.RegionsWrapper;

import io.reactivex.Observable;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public interface RegionRepository {
    Observable<RegionWrapper> getRegion(String apiToken, String regionId);
    Observable<RegionsWrapper> getRegions(String apiToken);
}
