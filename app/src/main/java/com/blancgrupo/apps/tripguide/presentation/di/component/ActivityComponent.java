package com.blancgrupo.apps.tripguide.presentation.di.component;

import com.blancgrupo.apps.tripguide.presentation.di.ActivityScope;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.ChooseLocationActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.CityDetailActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.GettingStartedActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.PlaceDetailActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SearchActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SplashScreenActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.TourActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.CitiesFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.MapFragment;
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
    void inject(CityDetailActivity activity);
    void inject(PlaceDetailActivity activity);
    void inject(SearchActivity activity);
    void inject(ToursFragment toursFragment);
    void inject(ChooseLocationActivity chooseLocationActivity);
    void inject(GettingStartedActivity gettingStartedActivity);
    void inject(LocationService locationService);
    void inject(TourActivity tourActivity);
}

