package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.MapFragment;
import com.blancgrupo.apps.tripguide.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    MapFragment mapFragment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            Bundle args = new Bundle();
            Bundle data = getIntent().getExtras();
            if (data != null && data.containsKey(Constants.EXTRA_PLACE_FOR_MAP)) {
                PlaceModel place = data.getParcelable(Constants.EXTRA_PLACE_FOR_MAP);
                if (place != null) {
                    toolbar.setTitle(place.getName());
                    actionBar.setTitle(place.getName());
                    args.putParcelable(Constants.EXTRA_PLACE_FOR_MAP, place);
                }
            }
            mapFragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
    }
}
