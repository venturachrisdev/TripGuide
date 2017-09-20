package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.app.SearchManager;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesCoverWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.SearchVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.SearchViewModel;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity implements PlaceAdapter.PlaceAdapterListener, LifecycleRegistryOwner {
    private LifecycleRegistry register = new LifecycleRegistry(this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SearchView searchView;
    @BindView(R.id.search_result_rv)
    ShimmerRecyclerView recyclerView;
    PlaceAdapter adapter;

    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    @Inject
    SearchVMFactory searchVMFactory;
    SearchViewModel searchViewModel;

    android.arch.lifecycle.Observer<PlacesCoverWrapper> observer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View emptyView = getLayoutInflater().inflate(R.layout.search_empty_layout, recyclerView, false);
        View errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceAdapter(this, PlaceAdapter.PLACE_VERTICAL_ADAPTER, getApplication());
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
        recyclerView.setAdapter(statesRecyclerViewAdapter);

        DaggerActivityComponent.builder()
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .activityModule(new ActivityModule())
                .build()
                .inject(this);

        observer = new android.arch.lifecycle.Observer<PlacesCoverWrapper>() {
            @Override
            public void onChanged(@Nullable PlacesCoverWrapper placesCoverWrapper) {
                recyclerView.hideShimmerAdapter();
                if (placesCoverWrapper != null && placesCoverWrapper.getPlaces() != null) {
                    if (placesCoverWrapper.getPlaces().size() > 0) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        adapter.updateData(placesCoverWrapper.getPlaces());
                    } else {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                } else {
                    if (!ConnectivityUtils.isConnected(getApplicationContext())) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                    } else {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                }
            }
        };

        searchViewModel = ViewModelProviders.of(this, searchVMFactory).get(SearchViewModel.class);
        searchViewModel.searchPlacesByType(null, null).observe(this, observer);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.requestFocus();
        final View closeSearchView = searchView.findViewById(R.id.search_close_btn);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false);
                return true;
            }
        });
        closeSearchView.setVisibility(View.GONE);
        RxSearchView.queryTextChanges(searchView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull CharSequence charSequence) {
                        String query = charSequence.toString();
                        if (query.length() == 0) {
                            closeSearchView.setVisibility(View.GONE);
                            recyclerView.hideShimmerAdapter();
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                        } else {
                            recyclerView.showShimmerAdapter();
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                            searchViewModel.loadPlacesByType(query, "");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Intent i = getIntent();
        Bundle data = i.getExtras();
        if (data != null && data.containsKey(Constants.EXTRA_SEARCH_PLACE_QUERY)) {
            String query = data.getString(Constants.EXTRA_SEARCH_PLACE_QUERY);
            searchView.setQuery(query, true);
            searchView.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // check if no view has focus:
            View v = getCurrentFocus();
            if (v != null) {
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaceClick(PlaceCover place) {
        if (place.getType().equals("locality") || place.getType().equals("country")) {
            Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
            intent.putExtra(Constants.EXTRA_CITY_ID, place.getGoogleId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(SearchActivity.this, PlaceDetailActivity.class);
            if (place.getId() != null) {
                intent.putExtra(Constants.EXTRA_PLACE_ID, place.getId());
            } else {
                intent.putExtra(Constants.EXTRA_PLACE_GOOGLE_ID, place.getGoogleId());
            }
            startActivity(intent);
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.register;
    }
}
