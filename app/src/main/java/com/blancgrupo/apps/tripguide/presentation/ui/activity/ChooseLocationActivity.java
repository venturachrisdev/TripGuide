package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.CitiesFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.ToursFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.service.LocationService;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseLocationActivity extends AppCompatActivity
        implements  CitiesFragment.CitiesFragmentListener, LocationListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Inject
    SharedPreferences sharedPreferences;
    CitiesFragment fragment;
    ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }


    public void goForCurrentLocation() {
        if (LocationUtils.checkForPermission(this)) {
            if (LocationUtils.isGpsEnabled(this)) {
                onGpsEnabled();
            } else {
                LocationUtils.showDialogEnableGps(this);
            }

        }
    }

    public void onGpsEnabled() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.show();
        Location location = LocationUtils.getCurrentLocation(this);
        if (location != null) {
            fragment.fetchCurrentCity(dialog, String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()));
        } else {
            LocationUtils.requestLocationUpdates(this, this);
            dialog.hide();
            Toast.makeText(this, R.string.cannot_find_your_location, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationUtils.PERMISSION_ENABLE_GPS_REQUEST_CODE) {
            if (LocationUtils.checkForPermission(this)) {
                if (LocationUtils.isGpsEnabled(this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onGpsEnabled();
                        }
                    }, 1400);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fragment == null) {
            fragment = new CitiesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCityClick(City city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.CURRENT_LOCATION_SP, city.getId());
        editor.apply();
        Intent i = new Intent(this, LocationService.class);
        startService(i);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        fragment.fetchCurrentCity(dialog, String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()));
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
