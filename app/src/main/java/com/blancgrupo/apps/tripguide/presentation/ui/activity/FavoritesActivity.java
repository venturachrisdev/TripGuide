package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity implements PlaceAdapter.PlaceAdapterListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.favorites_rv)
    ShimmerRecyclerView recyclerView;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    PlaceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceAdapter(this, PlaceAdapter.PLACE_VERTICAL_ADAPTER, getApplication());
        View emptyView = getLayoutInflater().inflate(R.layout.favorites_empty_layout, recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, null);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaceClick(PlaceCover place) {

    }
}
