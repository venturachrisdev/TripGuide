package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Tour;
import com.blancgrupo.apps.tripguide.data.entity.api.TourWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TimelinePlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SingleTourActivity extends AppCompatActivity
    implements LifecycleRegistryOwner, TimelinePlaceAdapter.PlaceTimeLineListener, OnMapReadyCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.places_rv)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.name_text)
    LoaderTextView nameText;
    @BindView(R.id.places_text)
    LoaderTextView placesText;
    @BindView(R.id.time_text)
    LoaderTextView timeText;
    @BindView(R.id.distance_text)
    LoaderTextView distanceText;

    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    TimelinePlaceAdapter adapter;
    @Inject
    TourVMFactory tourVMFactory;
    TourViewModel tourViewModel;
    private LifecycleRegistry registry = new LifecycleRegistry(this);
    private String imageUrl;
    GoogleMap googleMap;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tour);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.EXTRA_PLACE_ID)) {
                String title = extras.getString(Constants.EXTRA_PLACE_ID);
                getSupportActionBar().setTitle(title);
            }
        }
        DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        tourViewModel = ViewModelProviders.of(this, tourVMFactory)
                .get(TourViewModel.class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new TimelinePlaceAdapter(this);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, null, null);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        recyclerView.showShimmerAdapter();
        recyclerView.clearFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleMap == null) {
            final SupportMapFragment mapFragment = new SupportMapFragment();
            final OnMapReadyCallback callback = this;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.map, mapFragment)
                            .commit();
                    mapFragment.getMapAsync(callback);
                }
            });
        }
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data != null && data.containsKey(Constants.EXTRA_SINGLE_TOUR_ID)) {
            imageUrl = data.getString(Constants.EXTRA_IMAGE_URL);
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(headerImage);
            String id = data.getString(Constants.EXTRA_SINGLE_TOUR_ID);
            tourViewModel.getSingleTour(id).observe(this, new Observer<TourWrapper>() {
                @Override
                public void onChanged(@Nullable TourWrapper tourWrapper) {
                    recyclerView.hideShimmerAdapter();
                    if (tourWrapper != null) {
                        Tour tour = tourWrapper.getTour();
                        if (tour != null) {
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                            bindTour(tour);
                        } else {
                            //TODO: Handle
                            Toast.makeText(SingleTourActivity.this,
                                    tourWrapper.getStatus(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // TODO: Handle
                        Toast.makeText(SingleTourActivity.this,
                                R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindTour(final Tour tour) {
        nameText.setText(tour.getName());
        placesText.setText(String.valueOf(tour.getPlaces().size()));
        timeText.setText(ApiUtils.getTourTime(this, tour.getTotalTime()));
        distanceText.setText(ApiUtils.getTourDistance(this, tour.getTotalDistance()));
        adapter.updateData(tour.getPlaces());
        if (tour.getPlaces() != null) {
            if (googleMap != null) {
                bindMap(tour.getPlaces());
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (googleMap != null) {
                            bindMap(tour.getPlaces());
                        }
                    }
                }, 250);
            }
        }
    }

    private void bindMap(List<PlaceTypesCover> places) {
        Location first = places.get(0).getLocation();
        Location last =places.get(places.size() - 1).getLocation();
        double centerLat = (first.getLat() + last.getLat()) / 2;
        double centerLng = (first.getLng() + last.getLng()) / 2;
        PolylineOptions polylineOptions = new PolylineOptions();
        for (PlaceTypesCover cover : places) {
            LatLng where = new LatLng(cover.getLocation().getLat(), cover.getLocation().getLng());
            polylineOptions.add(where);
            googleMap.addMarker(new MarkerOptions().icon(ApiUtils.drawMarkerByType(
                    this, "place"
            )).position(where).title(cover.getName()));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(centerLat, centerLng), 14.6f));
        googleMap.addPolyline(polylineOptions
                .color(ContextCompat.getColor(this, R.color.colorAccent))
                .width(10)
                .zIndex(2)
                .geodesic(true));

    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }

    @Override
    public void onCardPlaceClick(PlaceTypesCover cover) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_ID, cover.getId());
        startActivity(intent);
    }

    @Override
    public void onCardPlaceLongClick(PlaceTypesCover cover, int position) {
        Toast.makeText(this, "Position " + position + " was clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
