package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.CountriesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Country;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.CountryAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CountryVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CountryViewModel;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryActivity extends AppCompatActivity implements CountryAdapter.CountryAdapterListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.countries_rv)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    CountryAdapter adapter;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    CountryVMFactory countryVMFactory;
    CountryViewModel countryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
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

        adapter = new CountryAdapter((MyApplication) getApplication(), this);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null,
                null, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        recyclerView.showShimmerAdapter();

        countryViewModel = ViewModelProviders.of(this, countryVMFactory)
                .get(CountryViewModel.class);

        final String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);

        countryViewModel.getCountries(apiToken).observe(this, new Observer<CountriesWrapper>() {
            @Override
            public void onChanged(@Nullable CountriesWrapper countriesWrapper) {
                recyclerView.hideShimmerAdapter();
                swipeRefreshLayout.setRefreshing(false);
                if (countriesWrapper != null && countriesWrapper.getCountries() != null) {
                    adapter.updateData(countriesWrapper.getCountries());
                } else {
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                countryViewModel.loadCountries(apiToken);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCountryItemClick(Country country) {

    }
}
