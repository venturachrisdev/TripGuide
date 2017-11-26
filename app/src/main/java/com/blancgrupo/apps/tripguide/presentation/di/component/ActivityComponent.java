package com.blancgrupo.apps.tripguide.presentation.di.component;

import com.blancgrupo.apps.tripguide.presentation.di.ActivityScope;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.AddReviewActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.ChooseLocationActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.CityToursActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.CountryActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.GettingStartedActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.HomeActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.PlaceDetailActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.RegionsActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SearchActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SingleTourActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.TourActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.ViewProfileActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.AccountFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.CitiesFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.CityDetailFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.FavoritesFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.MapFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.ProfileFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.SignInFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.ToursFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.service.LocationService;

import dagger.Component;

/**
 * Created by root on 8/15/17.
 */

@ActivityScope
@Component(dependencies = {NetComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(CitiesFragment fragment);
    void inject(MapFragment fragment);
    void inject(HomeActivity activity);
    void inject(PlaceDetailActivity activity);
    void inject(SearchActivity activity);
    void inject(ToursFragment toursFragment);
    void inject(ChooseLocationActivity chooseLocationActivity);
    void inject(GettingStartedActivity gettingStartedActivity);
    void inject(LocationService locationService);
    void inject(TourActivity tourActivity);
    void inject(CityToursActivity cityToursActivity);
    void inject(SingleTourActivity singleTourActivity);
    void inject(CityDetailFragment cityDetailFragment);
    void inject(ProfileFragment profileFragment);
    void inject(AddReviewActivity addReviewActivity);
    void inject(AccountFragment accountFragment);
    void inject(SignInFragment signInFragment);
    void inject(FavoritesFragment favoritesFragment);
    void inject(ViewProfileActivity viewProfileActivity);
    void inject(RegionsActivity regionsActivity);
    void inject(CountryActivity countryActivity);
}

