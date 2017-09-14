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
import android.view.View;
import android.widget.Button;
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
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SingleTourActivity extends AppCompatActivity
    implements LifecycleRegistryOwner, TimelinePlaceAdapter.PlaceTimeLineListener, OnMapReadyCallback, RoutingListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.places_rv)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.time_text)
    LoaderTextView timeText;
    @BindView(R.id.distance_text)
    LoaderTextView distanceText;

    @BindView(R.id.navigate_btn)
    Button navigateBtn;

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
        final boolean running = data.getBoolean(Constants.EXTRA_IS_TOUR_RUNNING);
        final int position = data.getInt(Constants.EXTRA_CURRENT_IMAGE_POSITION, 0);
        if (data != null && data.containsKey(Constants.EXTRA_SINGLE_TOUR_ID)) {
            imageUrl = data.getString(Constants.EXTRA_IMAGE_URL);
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(headerImage);
            final String id = data.getString(Constants.EXTRA_SINGLE_TOUR_ID);
            tourViewModel.getSingleTour(id).observe(this, new Observer<TourWrapper>() {
                @Override
                public void onChanged(@Nullable TourWrapper tourWrapper) {
                    recyclerView.hideShimmerAdapter();
                    if (tourWrapper != null) {
                        final Tour tour = tourWrapper.getTour();
                        if (tour != null) {
                            if (running) {
                                goRunningTour(tour, position, id);
                            }
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                            navigateBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goRunningTour(tour, position, id);
                                }
                            });
                            bindTour(tour);
                        } else {
                            //TODO: Handle
                            Toast.makeText(SingleTourActivity.this,
                                    tourWrapper.getStatus(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SingleTourActivity.this,
                                "tourWrapper es null", Toast.LENGTH_LONG).show();
                        // TODO: Handle
//                        Toast.makeText(SingleTourActivity.this,
//                                R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(SingleTourActivity.this,
                    "SINGLE_TOUR_ID NO SE ENCUENTRA", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    void goRunningTour(Tour tour, int position, String id) {
        if (LocationUtils.isGpsEnabled(this)) {
            Intent intent = new Intent(SingleTourActivity.this, RunningTourActivity.class);
            intent.putExtra(Constants.EXTRA_SINGLE_TOUR_PLACES, tour);
            intent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
            intent.putExtra(Constants.EXTRA_SINGLE_TOUR_ID, id);
            startActivity(intent);
        } else {
            LocationUtils.showDialogEnableGps(this);
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

        List<LatLng> waypoints = new ArrayList<>();
        for (PlaceTypesCover cover : places) {
            LatLng where = new LatLng(cover.getLocation().getLat(), cover.getLocation().getLng());
            waypoints.add(where);
            googleMap.addMarker(new MarkerOptions().icon(ApiUtils.drawMarkerByType(
                    this, "place"
            )).position(where).title(cover.getName()));
        }
        Routing routing = new Routing.Builder()
                .key(getString(R.string.google_maps_key))
                .travelMode(Routing.TravelMode.WALKING)
                .alternativeRoutes(false)
                .optimize(true)
                .withListener(this)
                .waypoints(waypoints)
                .build();
        routing.execute();
        Location first = places.get(0).getLocation();
        Location last =places.get(places.size() - 1).getLocation();
        double centerLat = (first.getLat() + last.getLat()) / 2;
        double centerLng = (first.getLng() + last.getLng()) / 2;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(centerLat, centerLng), 14.6f));

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

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> directions, int i) {
        if (googleMap != null) {
            for (Route route: directions) {
                googleMap.addPolyline(route.getPolyOptions()
                                .color(ContextCompat.getColor(this, R.color.colorAccent))
                );
            }
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
