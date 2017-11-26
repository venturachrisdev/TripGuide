package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Region;
import com.blancgrupo.apps.tripguide.data.entity.api.RegionsWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.CityAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.RegionAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.RegionVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.RegionViewModel;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionsActivity extends AppCompatActivity
    implements RegionAdapter.RegionAdapterListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.regions_rv)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    RegionAdapter adapter;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    RegionVMFactory regionVMFactory;
    RegionViewModel regionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regions);
        ButterKnife.bind(this);
        DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        adapter = new RegionAdapter((MyApplication) getApplication(), this);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null,
                null, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        recyclerView.showShimmerAdapter();

        regionViewModel = ViewModelProviders.of(this, regionVMFactory)
                .get(RegionViewModel.class);

        final String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);

        regionViewModel.getRegions(apiToken).observe(this, new Observer<RegionsWrapper>() {
            @Override
            public void onChanged(@Nullable RegionsWrapper regionsWrapper) {
                recyclerView.hideShimmerAdapter();
                swipeRefreshLayout.setRefreshing(false);
                if (regionsWrapper != null && regionsWrapper.getRegions() != null) {
                    adapter.updateData(regionsWrapper.getRegions());
                } else {
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                regionViewModel.loadRegions(apiToken);
            }
        });

    }

    @Override
    public void onRegionItemClick(Region region) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
