package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.MapFragment;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_search:
                //TODO: Near me recommendations
                Toast.makeText(this, "Comming soon, budy.", Toast.LENGTH_LONG).show();
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
                Place place = data.getParcelable(Constants.EXTRA_PLACE_FOR_MAP);
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
