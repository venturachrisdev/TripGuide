package com.blancgrupo.apps.tripguide.data;

import com.blancgrupo.apps.tripguide.data.entity.api.RegionWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.RegionsWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.RegionRepository;

import io.reactivex.Observable;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class ApiRegionRepository implements RegionRepository {
    ApiPlaceService apiPlaceService;

    public ApiRegionRepository(ApiPlaceService apiPlaceService) {
        this.apiPlaceService = apiPlaceService;
    }

    @Override
    public Observable<RegionWrapper> getRegion(String apiToken, String regionId) {
        return apiPlaceService.getRegion(apiToken, regionId);
    }

    @Override
    public Observable<RegionsWrapper> getRegions(String apiToken) {
        return apiPlaceService.getRegions(apiToken);
    }
}
