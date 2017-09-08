package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Topic;
import com.blancgrupo.apps.tripguide.data.entity.api.Tour;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TopicAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TourAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.custom.FeatureCardView;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CityDetailActivity extends AppCompatActivity
        implements LifecycleRegistryOwner, TextStringUtils.PlaceItemActivityListener, PlaceAdapter.PlaceAdapterListener, TopicAdapter.TopicListener, NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    CityViewModel cityViewModel;
    @Inject
    CityVMFactory cityVMFactory;
    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_map)
    FloatingActionButton fab_map;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.title)
    TextView toolbarTitle;
    @BindView(R.id.featurecardview)
    FeatureCardView featureCardView;
    @BindView(R.id.all_topics_rv)
    ShimmerRecyclerView recyclerView;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    TopicAdapter adapter;
    View errorView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbarTitle.setText(R.string.app_name);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        int transparent = ContextCompat
                .getColor(getApplicationContext(),android.R.color.transparent);
        toolbar.setTitleTextColor(transparent);
        toolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(transparent));
        toolbarLayout.setCollapsedTitleTextColor(ColorStateList.valueOf(transparent));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new TopicAdapter(this, this, getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        View emptyView = getLayoutInflater().inflate(R.layout.nothing_to_show_layout, recyclerView, false);
        errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
        ((TextView) errorView.findViewById(R.id.textView2)).setText(R.string.network_error_tap_to_retry);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        recyclerView.showShimmerAdapter();

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        fab_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LocationUtils.checkForPermissionOrRequest(CityDetailActivity.this)) {
                    if (LocationUtils.gpsEnabledOrShowDialog(CityDetailActivity.this)) {
                        startActivity(new Intent(CityDetailActivity.this, MapActivity.class));
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationUtils.PERMISSION_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (LocationUtils.gpsEnabledOrShowDialog(CityDetailActivity.this)) {
                    startActivity(new Intent(CityDetailActivity.this, MapActivity.class));
                }

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                LocationUtils.showAreYouSureLocation(CityDetailActivity.this);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LocationUtils.checkForPermission(this)) {
            LocationUtils.requestLocationUpdates(getApplicationContext(), this);
        }
        String id = null;
        Bundle data = getIntent().getExtras();
        if (data != null && data.containsKey(Constants.EXTRA_CITY_ID)) {
            id = data.getString(Constants.EXTRA_CITY_ID);
        } else {
            if (!sharedPreferences.contains(Constants.CURRENT_LOCATION_SP)) {
                Intent intent = new Intent(CityDetailActivity.this, ChooseLocationActivity.class);
                startActivityForResult(intent, Constants.CHOOSE_LOCATION_RC);
            } else {
                id = sharedPreferences.getString(Constants.CURRENT_LOCATION_SP, null);
            }
        }
        cityViewModel = ViewModelProviders.of(this, cityVMFactory)
                .get(CityViewModel.class);

        Observer<CityWrapper> observer = new Observer<CityWrapper>() {
            @Override
            public void onChanged(@Nullable CityWrapper cityWrapper) {
                // STOP PROGRESS
                recyclerView.hideShimmerAdapter();
                if (cityWrapper != null) {
                    if (cityWrapper.getCity() != null) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        bindCity(cityWrapper.getCity());
                    } else {
                        if (!ConnectivityUtils.isConnected(getApplicationContext())) {
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                        } else {
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                        }
                    }
                } else {
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                }
            }
        };

        if (cityViewModel.isSingleCityLoaded()) {
            cityViewModel.getLoadedSingleCity().observe(this, observer);
        } else {
            cityViewModel.getSingleCity(id).observe(this, observer);
        }

        if (errorView != null) {
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cityViewModel != null) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        recyclerView.showShimmerAdapter();
                        String id = sharedPreferences.getString(Constants.CURRENT_LOCATION_SP, null);
                        cityViewModel.loadSingleCity(id);
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void bindCity(City city) {
        recyclerView.hideShimmerAdapter();
        toolbarLayout.setTitle(city.getName());
        toolbarTitle.setText(city.getName());
        featureCardView.show();
        Photo header = city.getPhoto();

        if (header != null && header.getReference() != null) {
            final List<Photo> photos = new ArrayList<>();
            photos.add(header);
            String url = ApiUtils.getPlacePhotoUrl((MyApplication) getApplication(),
                    header.getReference(), header.getWidth());
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into(headerImage);

            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CityDetailActivity.this, DisplayImageActivity.class);
                    intent.putParcelableArrayListExtra(Constants.EXTRA_IMAGE_URL,
                            (ArrayList<? extends Parcelable>) photos);
                    startActivity(intent);
                }
            });
        }
        // BIND TOPICS
        List<Topic> topics = city.getTopics();
        if (topics.size() > 0) {
            if (topics.get(0).get_id().equals("tour")) {
                featureCardView.hide();
            }
            adapter.updateData(topics);
        } else {
            Intent intent = new Intent(this, PlaceDetailActivity.class);
            intent.putExtra(Constants.EXTRA_PLACE_GOOGLE_ID, city.getGoogleId());
            startActivity(intent);
            finish();
        }

    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }

    @Override
    public void onPlaceClick(PlaceCover place) {
        if (!place.getType().equals("tour")) {
            Intent intent = new Intent(CityDetailActivity.this, PlaceDetailActivity.class);
            intent.putExtra(Constants.EXTRA_PLACE_ID, place.getId());
            startActivity(intent);
        } else {
            // A Tour
            Intent intent = new Intent(this, TourActivity.class);
            intent.putExtra(Constants.EXTRA_PLACE_TOUR_ID, place.getId());
            startActivity(intent);
        }

    }

    @Override
    public void onMoreButtonClick(String topicTitle) {
        Intent intent = new Intent(this, SearchActivity.class);
        String topify = TextStringUtils.formatTitle(topicTitle);
        intent.putExtra(Constants.EXTRA_SEARCH_PLACE_QUERY, topify + " " +
                getString(R.string.in) + " " + toolbarLayout.getTitle());
        startActivity(intent);
    }

    @Override
    public void onTourPresence() {
        featureCardView.hide();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_location:
                Intent intent = new Intent(CityDetailActivity.this, ChooseLocationActivity.class);
                startActivityForResult(intent, Constants.CHOOSE_LOCATION_RC);
                break;
            case R.id.nav_favorite:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;
            case R.id.nav_share:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
        if (requestCode == Constants.CHOOSE_LOCATION_RC) {
            if (resultCode == RESULT_OK) {
                String id = sharedPreferences.getString(Constants.CURRENT_LOCATION_SP, null);
                cityViewModel.loadSingleCity(id);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
