package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Topic;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TopicAdapter;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CityDetailActivity extends AppCompatActivity
        implements LifecycleRegistryOwner, TextStringUtils.PlaceItemActivityListener, PlaceAdapter.PlaceAdapterListener, TopicAdapter.TopicListener {
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    CityViewModel cityViewModel;
    @Inject
    CityVMFactory cityVMFactory;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.all_topics_rv)
    ShimmerRecyclerView recyclerView;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    TopicAdapter adapter;

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
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        adapter = new TopicAdapter(this, this, getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        View emptyView = getLayoutInflater().inflate(R.layout.nothing_to_show_layout, recyclerView, false);
        View errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        recyclerView.showShimmerAdapter();

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (!data.containsKey(Constants.EXTRA_CITY_ID)) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            finish();
        }
        String id = data.getString(Constants.EXTRA_CITY_ID);
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
    }

    private void displayError(String status) {
        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
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

    void bindCity(City city) {
        recyclerView.hideShimmerAdapter();
        toolbarLayout.setTitle(city.getName());
        Photo header = city.getPhoto();

        if (header != null && header.getReference() != null) {
            final String url = ApiUtils.getPlacePhotoUrl((MyApplication) getApplication(),
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
                    intent.putExtra(Constants.EXTRA_IMAGE_URL, url);
                    startActivity(intent);
                }
            });
        }
        // BIND TOPICS
        List<Topic> topics = city.getTopics();
        if (topics.size() > 0) {
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
        Intent intent = new Intent(CityDetailActivity.this, PlaceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_ID, place.getId());
        startActivity(intent);
    }

    @Override
    public void onMoreButtonClick(String topicTitle) {
        Intent intent = new Intent(this, SearchActivity.class);
        String topify = TextStringUtils.formatTitle(topicTitle);
        intent.putExtra(Constants.EXTRA_SEARCH_PLACE_QUERY, topify + " in " + toolbarLayout.getTitle());
        startActivity(intent);
    }
}
