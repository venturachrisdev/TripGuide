package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TopicAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TourAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CityToursActivity extends AppCompatActivity
        implements LifecycleRegistryOwner, TourAdapter.TourListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tours_rv)
    ShimmerRecyclerView recyclerView;
    TourAdapter adapter;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    @Inject
    TourVMFactory tourVMFactory;
    TourViewModel tourViewModel;
    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_tours);
        ButterKnife.bind(this);
        DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication)getApplication()).getNetComponent())
                .build()
                .inject(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ImageView icon = findViewById(R.id.topic_icon);
            icon.setColorFilter(getApplication().getColor(R.color.colorAccent));
        }

        tourViewModel = ViewModelProviders.of(this, tourVMFactory).get(TourViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data != null && data.containsKey(Constants.EXTRA_CITY_ID)) {
            String cityId = data.getString(Constants.EXTRA_CITY_ID);
            recyclerView.setHasFixedSize(true);
            adapter = new TourAdapter(this, getApplication());
            View emptyView = getLayoutInflater().inflate(R.layout.nothing_to_show_layout, recyclerView, false);
            View errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
            statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(statesRecyclerViewAdapter);
            recyclerView.showShimmerAdapter();
            tourViewModel.getTours().observe(this, new Observer<PlacesWrapper>() {
                @Override
                public void onChanged(@Nullable PlacesWrapper placesCoverWrapper) {
                    if (placesCoverWrapper != null) {
                        List<Place> places = placesCoverWrapper.getPlaces();
                        if (places != null && places.size() > 0) {
                            toolbar.setTitle(places.get(0).getCity().getName());
                            recyclerView.hideShimmerAdapter();
                            adapter.updateData(places);
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        } else {
                            recyclerView.hideShimmerAdapter();
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                        }
                    } else {
                        recyclerView.hideShimmerAdapter();
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                    }
                }
            });
        } else {
            recyclerView.hideShimmerAdapter();
            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }

    @Override
    public void onTourClick(Place tour) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_ID, tour.getId());
        startActivity(intent);
    }
}
