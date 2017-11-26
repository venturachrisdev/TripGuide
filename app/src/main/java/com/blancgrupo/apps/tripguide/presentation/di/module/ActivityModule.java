package com.blancgrupo.apps.tripguide.presentation.di.module;

import com.blancgrupo.apps.tripguide.data.ApiCityRepository;
import com.blancgrupo.apps.tripguide.data.ApiCountryRepository;
import com.blancgrupo.apps.tripguide.data.ApiPlaceRepository;
import com.blancgrupo.apps.tripguide.data.ApiProfileRepository;
import com.blancgrupo.apps.tripguide.data.ApiRegionRepository;
import com.blancgrupo.apps.tripguide.data.ApiTourRepository;
import com.blancgrupo.apps.tripguide.data.GooglePlaceRepository;
import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.data.persistence.repository.PlaceDBRepository;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ProfileDBRepository;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ReviewDBRepository;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.blancgrupo.apps.tripguide.data.service.GooglePlaceService;
import com.blancgrupo.apps.tripguide.domain.repository.CityRepository;
import com.blancgrupo.apps.tripguide.domain.repository.CountryRepository;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.domain.repository.RegionRepository;
import com.blancgrupo.apps.tripguide.domain.repository.TourRepository;
import com.blancgrupo.apps.tripguide.presentation.di.ActivityScope;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CountryVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.RegionVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.SearchVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourVMFactory;

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
    TourRepository providesTourRepository(ApiPlaceService apiPlaceService) {
        return new ApiTourRepository(apiPlaceService);
    }

    @Provides
    @ActivityScope
    ProfileRepository providesProfileRepository(ApiPlaceService apiPlaceService) {
        return new ApiProfileRepository(apiPlaceService);
    }

    @Provides
    @ActivityScope
    RegionRepository providesRegionRepository(ApiPlaceService apiPlaceService) {
        return new ApiRegionRepository(apiPlaceService);
    }

    @Provides
    @ActivityScope
    CountryRepository providesCountryRepository(ApiPlaceService apiPlaceService) {
        return new ApiCountryRepository(apiPlaceService);
    }

    @Provides
    @ActivityScope
    CountryVMFactory providesCountryVMFactory(CountryRepository countryRepository) {
        return new CountryVMFactory(countryRepository);
    }

    @Provides
    @ActivityScope
    RegionVMFactory providesRegionVMFactory(RegionRepository regionRepository) {
        return new RegionVMFactory(regionRepository);
    }

    @Provides
    @ActivityScope
    ProfileVMFactory providesProfileFactory(ProfileRepository profileRepository) {
        return new ProfileVMFactory(profileRepository);
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
    PlaceVMFactory providesPlaceFactory(PlaceRepository placeRepository) {
        return new PlaceVMFactory(placeRepository);
    }
    @Provides
    @ActivityScope
    TourVMFactory providesTourFactory(TourRepository tourRepository) {
        return new TourVMFactory(tourRepository);
    }

    @Provides
    @ActivityScope
    ProfileDBRepository providesProfileDBRepository(PlacesDatabase placesDatabase) {
        return new ProfileDBRepository(placesDatabase);
    }

    @Provides
    @ActivityScope
    PlaceDBRepository providesPlacesDBRepository(PlacesDatabase placesDatabase) {
        return new PlaceDBRepository(placesDatabase);
    }

    @Provides
    @ActivityScope
    ReviewDBRepository providesReviewsDBRepository(PlacesDatabase placesDatabase) {
        return new ReviewDBRepository(placesDatabase);
    }


}

