package com.blancgrupo.apps.tripguide.presentation.di.module;

import com.blancgrupo.apps.tripguide.data.ApiCityRepository;
import com.blancgrupo.apps.tripguide.data.ApiPlaceRepository;
import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.data.service.GooglePlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.CityRepository;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.presentation.di.ActivityScope;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.SearchVMFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by root on 8/15/17.
 */
@Module
public class ActivityModule {

    @Provides
    @ActivityScope
    GooglePlaceService providesGoogleService(@Named("GoogleRetrofit") Retrofit retrofit) {
        return retrofit.create(GooglePlaceService.class);
    }

    @Provides
    @ActivityScope
    ApiPlaceService providesApiService(@Named("ApiRetrofit") Retrofit retrofit) {
        return retrofit.create(ApiPlaceService.class);
    }

    @Provides
    @ActivityScope
    CityRepository providesCityRepository(ApiPlaceService apiPlaceService) {
        return new ApiCityRepository(apiPlaceService);
    }

    @Provides
    @ActivityScope
    PlaceRepository providesPlaceRepository(ApiPlaceService apiPlaceService) {
        return new ApiPlaceRepository(apiPlaceService);
    }

    @Provides
    @ActivityScope
    CityVMFactory providesCityFactory(CityRepository cityRepository) {
        return new CityVMFactory(cityRepository);
    }

    @Provides
    @ActivityScope
    GooglePlaceRepository providesGooglePlaceRepository(GooglePlaceService googlePlaceService) {
        return new GooglePlaceRepository(googlePlaceService);
    }

    @Provides
    @ActivityScope
    SearchVMFactory providesSearchFactory(GooglePlaceRepository googlePlaceRepository) {
        return new SearchVMFactory(googlePlaceRepository);
    }

    @Provides
    @ActivityScope
    PlaceVMFactory providesPlaceFactory(PlaceRepository placeRepository,
                                        GooglePlaceRepository googlePlaceRepository) {
        return new PlaceVMFactory(placeRepository, googlePlaceRepository);
    }


}

