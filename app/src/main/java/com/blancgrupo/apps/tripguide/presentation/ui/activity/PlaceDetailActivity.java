package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceDescription;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceDescriptionWrapper;
import com.blancgrupo.apps.tripguide.data.persistence.PlacesDatabase;
import com.blancgrupo.apps.tripguide.data.persistence.repository.PlaceDBRepository;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ReviewDBRepository;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.PlaceWithReviews;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;
import com.blancgrupo.apps.tripguide.domain.repository.PlaceRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PhotoAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.custom.InfoView;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PlaceDetailActivity extends AppCompatActivity
        implements LifecycleRegistryOwner, OnMapReadyCallback, PhotoAdapter.PhotoListener {
    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.share_btn)
    Button shareBtn;
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
    @BindView(R.id.photos_rv)
    ShimmerRecyclerView photosRecyclerView;
    @BindView(R.id.photos_layout)
    LinearLayout photosLayout;
    @BindView(R.id.rating_layout)
    RelativeLayout ratingLayout;
    @BindView(R.id.rating_bar)
    SimpleRatingBar ratingBar;
    @BindView(R.id.reviews_count)
    TextView reviewsCountText;
    @BindView(R.id.rating_toolbar)
    SimpleRatingBar ratingToolbar;
    @BindView(R.id.info_layout)
    InfoView infoView;
    @BindView(R.id.favorite_btn)
    Button favoriteBtn;

    List<Photo> myPhotos;

    @BindView(R.id.reviews_rv)
    ShimmerRecyclerView reviewsRecyclerView;

    @Inject
    PlaceVMFactory placeVMFactory;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    PlaceRepository placeRepository;


    @Inject
    PlaceDBRepository placeDBRepository;
    @Inject
    ReviewDBRepository reviewDBRepository;

    PlaceViewModel placeViewModel;
    GoogleMap map;
    PlaceModel place;

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
        final String placeId;
        if (!data.containsKey(Constants.EXTRA_PLACE_ID)) {
            if (!data.containsKey(Constants.EXTRA_PLACE_GOOGLE_ID)) {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            placeId = data.getString(Constants.EXTRA_PLACE_GOOGLE_ID);
        } else {
            placeId = data.getString(Constants.EXTRA_PLACE_ID);
        }

        placeViewModel = ViewModelProviders.of(this, placeVMFactory)
                .get(PlaceViewModel.class);
        getPlaceFromDB(placeId);
    }

    private void getPlaceFromDB(final String placeId) {
        placeDBRepository.getPlaceById(placeId).observe(this, new Observer<PlaceWithReviews>() {
            @Override
            public void onChanged(@Nullable PlaceWithReviews placeWithReviews) {
                if (placeWithReviews != null && placeWithReviews.getPlace() != null) {
                    // is in database
                    bindPlace(placeWithReviews);
                } else {
                    // fetch from API
                    fetchPlaceFromAPI(placeId);
                }
            }
        });
    }

    private void savePlaceToDB(PlaceWithReviews placeWithReviews) {
        placeDBRepository.insertPlace(placeWithReviews.getPlace());
        reviewDBRepository.insertReview(placeWithReviews.getReviews());
    }

    private void updatePlaceToDB(PlaceModel place) {
        placeDBRepository.updatePlace(place);
    }


    private void fetchPlaceFromAPI(String placeId) {
        String apiToken = sharedPreferences
                .getString(Constants.USER_LOGGED_API_TOKEN_SP, null);

        Observer<PlaceWithReviews> observer = new Observer<PlaceWithReviews>() {
            @Override
            public void onChanged(@Nullable PlaceWithReviews place) {
                // STOP PROGRESS
                if (place != null && place.getPlace() != null) {
                    savePlaceToDB(place);
                    bindPlace(place);
                } else {
                    displayError("NULL_RESPONSE");
                }
            }
        };

        if (placeViewModel.isPlaceLoaded()) {
            placeViewModel.getLoadedSinglePlace().observe(this, observer);
        } else {
            placeViewModel.getSinglePlace(placeId, apiToken).observe(this, observer);
            placeViewModel.getPlaceDescription(placeId).observe(this, new Observer<PlaceDescriptionWrapper>() {
                @Override
                public void onChanged(@Nullable PlaceDescriptionWrapper placeDescriptionWrapper) {
                    if (placeDescriptionWrapper != null) {
                        PlaceDescription description = placeDescriptionWrapper.getPlaceDescription();
                        if (placeDescriptionWrapper.getStatus().equals("OK") && description != null
                                && description.getText() != null) {
                            infoView.setVisibility(View.VISIBLE);
                            infoView.setContentText(description.getText());
                        } else {
                            infoView.setVisibility(View.GONE);
                        }
                    } else {
                        infoView.setVisibility(View.GONE);
                    }
                }
            });
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

    @Override
    protected void onResume() {
        super.onResume();
        ratingBar.setRating(0);
    }

    File getPhotoFile(String photo) {
        try {
            return Glide.with(this)
                    .load(photo + ((MyApplication) getApplication()).getApiKey())
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch(Exception e) {
            e.printStackTrace();
            Log.w("SHARE", "Sharing image failed");
            return null;
        }
    }

    void sharePlace(final PlaceModel place) {
        final String photo = place.getPhotoUrl();
        Observable<File> saveImage = Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return getPhotoFile(photo);
            }
        });
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle(R.string.please_wait);
        dialog.show();
        saveImage
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull File file) throws Exception {
                        dialog.hide();
                        dialog.cancel();
                        Uri uri = FileProvider.getUriForFile(PlaceDetailActivity.this,
                                "com.blancgrupo.apps.tripguide.ImageFileProvider", file);
                        shareImage(uri, place);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dialog.hide();
                        dialog.cancel();
                        Toast.makeText(PlaceDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    void shareImage(Uri uri, PlaceModel place) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TITLE, place.getName());
        intent.putExtra(Intent.EXTRA_SUBJECT, place.getName());
        intent.putExtra(Intent.EXTRA_TEXT, String.format(
                getString(R.string.share_text),
                place.getName(),
                place.getCity(),
                "http://tripguide.com/place/" + place.get_id())
        );
        Intent chooser = Intent.createChooser(intent, getString(R.string.share));
        startActivity(chooser);
    }

    void bindPlace(@NonNull final PlaceWithReviews placeWithReviews) {
        final PlaceModel place = placeWithReviews.getPlace();
        List<ReviewModel> reviews = placeWithReviews.getReviews();
        toolbarLayout.setTitle(place.getName());
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePlace(place);
            }
        });
        if (place.getCity() != null) {
            toolbar.setSubtitle(place.getCity());
        }
        String description = place.getDescription();
        if (description != null && description.length() != 0) {
            infoView.setVisibility(View.VISIBLE);
            infoView.setContentText(description);
        }
        addressText.setText(place.getAddress());
        this.place = place;

        if (place.isFavorite()) {
            favoriteBtn.setText(R.string.remove_from_favorites);
            favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_accent_24dp
            ), null, null);
        } else {
            favoriteBtn.setText(R.string.add_to_favorite);
            favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_border_black_24dp
            ), null, null);
        }
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlaceAsFavorite(place);
            }
        });
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse(String.format("geo:%s,%s", place.getLat(),
                        place.getLng()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(PlaceDetailActivity.this, R.string.please_install_google_maps, Toast.LENGTH_SHORT).show();
                }

            }
        });
        addressLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(getString(R.string.address), place.getAddress());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), R.string.address_copied_to_clipboard, Toast.LENGTH_LONG)
                        .show();
                return true;
            }
        });
        if (place.getPhoneNumber() != null) {
            phoneLayout.setVisibility(View.VISIBLE);
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
            phoneLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(getString(R.string.phone_number), place.getPhoneNumber());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getApplicationContext(), R.string.phone_copied_to_clipboard, Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
            });
        }

        ratingLayout.setVisibility(View.VISIBLE);
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    Intent intent = new Intent(PlaceDetailActivity.this, AddReviewActivity.class);
                    intent.putExtra(Constants.EXTRA_PLACE_ID, place.get_id());
                    intent.putExtra(Constants.EXTRA_PROGRESS, simpleRatingBar.getRating());
                    intent.putExtra(Constants.EXTRA_PLACE_NAME, place.getName());
                    startActivityForResult(intent, 999);
                }
            }
        });
        ratingToolbar.setEnabled(false);
        ratingToolbar.setActivated(false);
        ratingToolbar.setRating((float) place.getRating());

        categoryText.setText(TextStringUtils.formatTitle(place.getType()));
//            if (weekend != null && weekend.size() > 0) {
//                calendarLayout.setVisibility(View.VISIBLE);
//                calendarLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new MaterialDialog.Builder(PlaceDetailActivity.this)
//                                .content(TextStringUtils.arrayToString(weekend))
//                                .show();
//                    }
//                });
//                switch (day) {
//                    case Calendar.MONDAY:
//                        calendarText.setText(weekend.get(0));
//                        break;
//                    case Calendar.TUESDAY:
//                        calendarText.setText(weekend.get(1));
//                        break;
//                    case Calendar.WEDNESDAY:
//                        calendarText.setText(weekend.get(2));
//                        break;
//                    case Calendar.THURSDAY:
//                        calendarText.setText(weekend.get(3));
//                        break;
//                    case Calendar.FRIDAY:
//                        calendarText.setText(weekend.get(4));
//                        break;
//                    case Calendar.SATURDAY:
//                        calendarText.setText(weekend.get(5));
//                        break;
//                    case Calendar.SUNDAY:
//                        calendarText.setText(weekend.get(6));
//                        break;
//
//                }
//            }
//        }
            String url = place.getPhotoUrl() + ((MyApplication) getApplication()).getApiKey();
            final List<Photo> photos = new ArrayList<>();
//            photos.add(photo);
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into(headerImage);

            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlaceDetailActivity.this, DisplayImageActivity.class);
                    intent.putParcelableArrayListExtra(Constants.EXTRA_IMAGE_URL,
                            (ArrayList<? extends Parcelable>) photos);
                    startActivity(intent);

                }
            });
//        List<Photo> photos = place.getPhotos();
//        if (photos != null && photos.size() > 0) {
//            myPhotos = photos;
//            photosLayout.setVisibility(View.VISIBLE);
//            PhotoAdapter adapter = new PhotoAdapter(getApplication(), this, photos);
//            photosRecyclerView.setHasFixedSize(true);
//            photosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//            photosRecyclerView.setAdapter(adapter);
//        }
        if (place.getWebsite() != null) {
            websiteLayout.setVisibility(View.VISIBLE);
            websiteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(place.getWebsite()));
                    startActivity(intent);
                }
            });
            websiteLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(getString(R.string.website), place.getWebsite());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getApplicationContext(), R.string.website_copied_to_clipboard, Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
            });
            websiteText.setText(place.getWebsite());
        }
        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = String.format("%s,%s", place.getLat(),
                        place.getLng());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + location));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(PlaceDetailActivity.this, R.string.please_install_google_maps, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (LocationUtils.checkForPermission(this)) {
            String distance = LocationUtils.measureDistance(
                    getApplicationContext(),
                    LocationUtils.getCurrentLocation(this),
                    place.getLat(),
                    place.getLng()
            );
            if (distance != null && distance.length() > 0) {
                distanceLayout.setVisibility(View.VISIBLE);
                distanceText.setText(distance);
                distanceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String location = String.format("%s,%s", place.getLat(),
                                place.getLng());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + location));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(PlaceDetailActivity.this, R.string.please_install_google_maps, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        if (place.get_id() == null || place.getCreatedAt() == null || place.isUserHasReviewed()) {
            ratingLayout.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        }

        // Reviews
        View emptyView1 = getLayoutInflater()
                .inflate(R.layout.empty_reviews_layout, reviewsRecyclerView, false);
        ReviewAdapter reviewAdapter = new ReviewAdapter(ReviewAdapter.REVIEW_PLACE_TYPE, null, null);
        StatesRecyclerViewAdapter statesRecyclerViewAdapter1 = new StatesRecyclerViewAdapter(
                reviewAdapter, null, emptyView1, null);
        //reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewsRecyclerView.setAdapter(statesRecyclerViewAdapter1);
        if (reviews != null && reviews.size() > 0) {
            reviewsCountText.setVisibility(View.VISIBLE);
            reviewsCountText.setText(String.format(getString(R.string.reviews_count), reviews.size()));
            reviewAdapter.updateData(reviews);
            statesRecyclerViewAdapter1.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
        } else {
            reviewsRecyclerView.hideShimmerAdapter();
            statesRecyclerViewAdapter1.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        }
        updateMap(place);
    }

    private void setPlaceAsFavorite(PlaceModel place) {
        String tokenId = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
        if (tokenId != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage(getString(R.string.please_wait));
            dialog.show();
            place.setFavorite(place.isFavorite());
            updatePlaceToDB(place);
            Place dummyPlace = new Place();
            dummyPlace.setId(place.get_id());
            placeRepository.addToMyFavorites(tokenId, dummyPlace)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                            if (s.equals("added")) {
                                Toast.makeText(PlaceDetailActivity.this, R.string.added_to_favorites,
                                        Toast.LENGTH_LONG).show();
                                favoriteBtn.setText(R.string.remove_from_favorites);
                                favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(
                                        PlaceDetailActivity.this, R.drawable.ic_favorite_accent_24dp
                                ), null, null);
                            } else {
                                Toast.makeText(PlaceDetailActivity.this, R.string.removed_from_favorites,
                                        Toast.LENGTH_LONG).show();
                                favoriteBtn.setText(R.string.add_to_favorite);
                                favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(
                                        PlaceDetailActivity.this, R.drawable.ic_favorite_border_black_24dp
                                ), null, null);
                            }
                            dialog.hide();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Toast.makeText(PlaceDetailActivity.this, R.string.network_error, Toast.LENGTH_LONG)
                                    .show();
                            dialog.hide();
                        }
                    });
        } else {
            Toast.makeText(PlaceDetailActivity.this, R.string.you_are_not_logged_in,
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (place != null) {
                String apiToken = sharedPreferences
                        .getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
                placeViewModel.loadSinglePlace(place.get_id(), apiToken);
            }
        }
    }

    void openMap(@NonNull PlaceModel place) {
        Intent intent = new Intent(PlaceDetailActivity.this, MapActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_FOR_MAP, place);
        startActivity(intent);
    }



    void mapLocation(@NonNull GoogleMap map, @NonNull final PlaceModel place) {
        if (place.getLat() != 0.0 && place.getLng() != 0.0) {
            findViewById(R.id.map).setVisibility(View.VISIBLE);
            LatLng where = new LatLng(place.getLat(), place.getLng());
            map.addMarker(new MarkerOptions()
                    .title(place.getName())
                    .snippet(place.getAddress())
                    .position(where)
                    .icon(ApiUtils.drawMarkerByType(getApplicationContext() , place.getType()))
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
    }

    private void updateMap(@NonNull final PlaceModel place) {
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
    protected void onStop() {
        super.onStop();
        placeDBRepository.onStop();
        reviewDBRepository.onStop();
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

    @Override
    public void onPhotoListener(Photo photo, int position) {
        Intent intent = new Intent(PlaceDetailActivity.this, DisplayImageActivity.class);
        intent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
        intent.putParcelableArrayListExtra(Constants.EXTRA_IMAGE_URL, (ArrayList<? extends Parcelable>) myPhotos);
        startActivity(intent);
    }
}
