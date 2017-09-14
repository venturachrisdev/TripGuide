package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Tour;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.RunningPlaceFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.service.LocationService;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RunningTourActivity extends AppCompatActivity
    implements ApiUtils.RunningPlaceListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private String tourId;

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
        Tour tour = data.getParcelable(Constants.EXTRA_SINGLE_TOUR_PLACES);
        int position = data.getInt(Constants.EXTRA_CURRENT_IMAGE_POSITION);
        tourId = data.getString(Constants.EXTRA_SINGLE_TOUR_ID);
        if (tour != null) {
            viewPager.setAdapter(new RunningPlaceViewPager(getSupportFragmentManager(), tour.getPlaces()));
            viewPager.setOffscreenPageLimit(0);
            viewPager.beginFakeDrag();
            viewPager.setCurrentItem(position, true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_next) {
            int currentPos = viewPager.getCurrentItem();
            int limit = viewPager.getAdapter().getCount() - 1;
            if (currentPos < limit) {
                viewPager.setCurrentItem(currentPos + 1, true);
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


    class RunningPlaceViewPager extends FragmentStatePagerAdapter {

        private final List<PlaceTypesCover> places;


        public RunningPlaceViewPager(FragmentManager supportFragmentManager, List<PlaceTypesCover> places) {
            super(supportFragmentManager);
            this.places = places;
        }

        @Override
        public Fragment getItem(int position) {
            PlaceTypesCover place = places.get(position);
            RunningPlaceFragment fragment = new RunningPlaceFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.EXTRA_PLACE_ID, place);
            args.putInt(Constants.EXTRA_CURRENT_POSITION, position + 1);
            args.putString(Constants.EXTRA_SINGLE_TOUR_ID, tourId);
            args.putInt(Constants.EXTRA_TOTAL, getCount());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            if (places != null) {
                return places.size();
            }
            return 0;
        }
    }
}
