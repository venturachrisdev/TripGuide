package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Tour;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.RunningPlaceFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.receiver.MyReceiver;
import com.blancgrupo.apps.tripguide.presentation.ui.service.LocationService;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RunningTourActivity extends AppCompatActivity
    implements ApiUtils.RunningPlaceListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String tourId;
    int currentPosition = 0;
    Tour tour;
    private double currentDistance;
    RunningPlaceFragment fragment;
    int currentProgress;
    double startDistance = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopService(new Intent(this, LocationService.class));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_tour);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle data = getIntent().getExtras();
        if (data == null || !data.containsKey(Constants.EXTRA_SINGLE_TOUR_PLACES)) {
            Toast.makeText(this, R.string.no_tour_selected, Toast.LENGTH_SHORT).show();
            finish();
        }
        tour = data.getParcelable(Constants.EXTRA_SINGLE_TOUR_PLACES);
        currentPosition = data.getInt(Constants.EXTRA_CURRENT_IMAGE_POSITION, 1);
        currentDistance = data.getDouble(Constants.EXTRA_CURRENT_DISTANCE);
        tourId = data.getString(Constants.EXTRA_SINGLE_TOUR_ID);
        currentProgress = data.getInt(Constants.EXTRA_PROGRESS);
        startDistance = data.getDouble(Constants.EXTRA_START_POSITION);
        if (tour != null) {
            if (currentPosition < tour.getPlaces().size() - 1) {
                setContentFragment(tour, currentPosition, currentDistance, currentProgress, startDistance);
            } else {
                Toast.makeText(this, "Position: " + currentPosition, Toast.LENGTH_SHORT).show();
                FrameLayout parent = findViewById(R.id.content);
                getLayoutInflater().inflate(R.layout.nothing_to_show_layout, parent, false);
            }
        }
        registerReceiver(new MyReceiver(), new IntentFilter("android.intent.action.RUN"));

    }

    void setContentFragment(Tour tour, int position, double  currentDistance, int currentProgress, double startDistance) {
        PlaceTypesCover cover = tour.getPlaces().get(position);
        Intent backgroundIntent = new Intent(getApplicationContext(), LocationService.class);
        backgroundIntent.setAction("android.intent.action.RUN");
        backgroundIntent.putExtra(Constants.EXTRA_PLACE_ID, cover);
        if (startDistance == 0) {
            this.startDistance = startDistance =
                    LocationUtils.measureDoubleDistance(this, LocationUtils.getCurrentLocation(this),
                    cover.getLocation().getLat(), cover.getLocation().getLng());

        }
        backgroundIntent.putExtra(Constants.EXTRA_START_POSITION, startDistance);
        backgroundIntent.putExtra(Constants.EXTRA_CURRENT_POSITION, currentDistance);
        backgroundIntent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, position);
        backgroundIntent.putExtra(Constants.EXTRA_SINGLE_TOUR_ID, tourId);
        backgroundIntent.putExtra(Constants.EXTRA_PROGRESS, currentProgress);
        Toast.makeText(this, "Started service", Toast.LENGTH_SHORT).show();
        startService(backgroundIntent);
        PlaceTypesCover place = tour.getPlaces().get(position);
        fragment = new RunningPlaceFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_PLACE_ID, place);
        args.putInt(Constants.EXTRA_CURRENT_POSITION, position);
        args.putString(Constants.EXTRA_SINGLE_TOUR_ID, tourId);
        args.putInt(Constants.EXTRA_TOTAL, tour.getPlaces().size());
        args.putDouble(Constants.EXTRA_CURRENT_DISTANCE, currentDistance);
        args.putInt(Constants.EXTRA_PROGRESS, currentProgress);
        args.putDouble(Constants.EXTRA_START_POSITION, startDistance);
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(new Intent(this, LocationService.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_next) {
            if (currentPosition < tour.getPlaces().size() - 1) {
                currentPosition += 1;
                setContentFragment(tour, currentPosition, currentDistance, 0, startDistance);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRunningPlaceClick(PlaceTypesCover cover) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_ID, cover.getId());
        startActivity(intent);
    }

}
