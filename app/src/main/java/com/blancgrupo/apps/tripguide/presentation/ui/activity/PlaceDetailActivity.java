package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.OpeningHours;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PlaceDetailActivity extends AppCompatActivity
        implements LifecycleRegistryOwner, OnMapReadyCallback {
    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.address_text)
    LoaderTextView addressText;
    @BindView(R.id.distance_text)
    LoaderTextView distanceText;
    @BindView(R.id.phone_icon)
    ImageView phoneIcon;
    @BindView(R.id.phone_text)
    LoaderTextView phoneText;
    @BindView(R.id.calendar_text)
    LoaderTextView calendarText;
    @BindView(R.id.category_text)
    LoaderTextView categoryText;
    @BindView(R.id.navigate_btn)
    Button navigateBtn;
    @BindView(R.id.address_layout)
    ConstraintLayout addressLayout;
    @BindView(R.id.distance_layout)
    ConstraintLayout distanceLayout;
    @BindView(R.id.phone_layout)
    ConstraintLayout phoneLayout;
    @BindView(R.id.category_layout)
    ConstraintLayout categoryLayout;
    @BindView(R.id.calendar_layout)
    ConstraintLayout calendarLayout;
    @BindView(R.id.website_layout)
    ConstraintLayout websiteLayout;
    @BindView(R.id.website_text)
    LoaderTextView websiteText;

    @Inject
    PlaceVMFactory placeVMFactory;

    PlaceViewModel placeViewModel;
    GoogleMap map;
    Place place;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String placeId;
        boolean fromGoogle = false;
        if (!data.containsKey(Constants.EXTRA_PLACE_ID)) {
            if (!data.containsKey(Constants.EXTRA_PLACE_GOOGLE_ID)) {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            placeId = data.getString(Constants.EXTRA_PLACE_GOOGLE_ID);
            fromGoogle = true;
        } else {
            placeId = data.getString(Constants.EXTRA_PLACE_ID);
        }

        Observer<PlaceWrapper> observer = new Observer<PlaceWrapper>() {
            @Override
            public void onChanged(@Nullable PlaceWrapper placeWrapper) {
                // STOP PROGRESS
                if (placeWrapper != null) {
                    if (placeWrapper.getPlace() != null) {
                        bindPlace(placeWrapper.getPlace());
                    } else {
                        displayError(placeWrapper.getStatus());
                    }
                } else {
                    displayError("NULL_RESPONSE");
                }
            }
        };

        placeViewModel = ViewModelProviders.of(this, placeVMFactory)
                .get(PlaceViewModel.class);
        if (placeViewModel.isPlaceLoaded()) {
            placeViewModel.getLoadedSinglePlace().observe(this, observer);
        } else {
            placeViewModel.getSinglePlace(placeId, fromGoogle).observe(this, observer);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void displayError(String status) {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (map == null) {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    void bindPlace(@NonNull final Place place) {
        toolbarLayout.setTitle(place.getName());
        addressText.setText(place.getAddress());
        this.place = place;
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse(String.format("geo:%s,%s", place.getLocation().getLat(),
                        place.getLocation().getLng()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });
        if (place.getPhoneNumber() != null) {
            phoneText.setText(place.getPhoneNumber());
            phoneLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL,
                            Uri.parse(String.format("tel:%s", place.getPhoneNumber()) )
                            )
                    );
                }
            });
        }
        categoryText.setText(TextStringUtils.formatTitle(place.getTypes().get(0)));
        OpeningHours opening = place.getOpeningHours();
        if (opening != null) {
            List<String> weekend = opening.getWeekdays();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekend != null && weekend.size() > 0) {
                switch (day) {
                    case Calendar.MONDAY:
                        calendarText.setText(weekend.get(0));
                        break;
                    case Calendar.TUESDAY:
                        calendarText.setText(weekend.get(1));
                        break;
                    case Calendar.WEDNESDAY:
                        calendarText.setText(weekend.get(2));
                        break;
                    case Calendar.THURSDAY:
                        calendarText.setText(weekend.get(3));
                        break;
                    case Calendar.FRIDAY:
                        calendarText.setText(weekend.get(4));
                        break;
                    case Calendar.SATURDAY:
                        calendarText.setText(weekend.get(5));
                        break;
                    case Calendar.SUNDAY:
                        calendarText.setText(weekend.get(6));
                        break;

                }
            }
        }
        Photo photo = place.getPhoto();
        if (photo != null && photo.getReference() != null) {
            final String url = ApiUtils.getPlacePhotoUrl((MyApplication) getApplication(),
                    photo.getReference(), photo.getWidth());
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into(headerImage);

            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlaceDetailActivity.this, DisplayImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_URL, url);
                    startActivity(intent);

                }
            });
        }
        if (place.getWebsite() != null) {
            websiteText.setText(place.getWebsite());
            websiteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(place.getWebsite()));
                    startActivity(intent);
                }
            });
        }
        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = String.format("%s,%s", place.getLocation().getLat(),
                        place.getLocation().getLng());
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + location)));
            }
        });
        distanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = String.format("%s,%s", place.getLocation().getLat(),
                        place.getLocation().getLng());
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + location)));
            }
        });
        updateMap(place);
    }

    void openMap(@NonNull Place place) {
        Intent intent = new Intent(PlaceDetailActivity.this, MapActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_FOR_MAP, place);
        startActivity(intent);
    }



    void mapLocation(@NonNull GoogleMap map, @NonNull final Place place) {
        LatLng where = new LatLng(place.getLocation().getLat(), place.getLocation().getLng());
        map.addMarker(new MarkerOptions()
                .title(place.getName())
                .snippet(place.getAddress())
                .position(where)
                .icon(ApiUtils.drawMarkerByType(getApplicationContext() , place.getTypes().get(0)))
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(where, 18f));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                openMap(place);
                return true;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                openMap(place);
            }
        });
    }

    private void updateMap(@NonNull final Place place) {
        if (map != null) {
            mapLocation(map, place);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (map != null) {
                        mapLocation(map, place);
                    }
                }
            }, 100);
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setIndoorEnabled(true);
        map.setTrafficEnabled(true);
        if (place != null) {
            mapLocation(map, place);
        }
    }
}
