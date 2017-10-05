package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.persistence.repository.PlaceDBRepository;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.domain.model.mapper.PlaceModelMapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.PlaceDetailActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.PlaceViewModel;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends LifecycleFragment
        implements PlaceAdapter.PlaceAdapterListener {
    @BindView(R.id.favorites_rv)
    ShimmerRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    PlaceAdapter adapter;

    @Inject
    PlaceVMFactory placeVMFactory;
    PlaceViewModel placeViewModel;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    PlaceDBRepository placeDBRepository;

    Observer<PlacesWrapper> observer;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, v);
        DaggerActivityComponent.builder()
                .netComponent(((MyApplication) getActivity().getApplication()).getNetComponent())
                .activityModule(new ActivityModule())
                .build()
                .inject(this);
        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new PlaceAdapter(this, PlaceAdapter.PLACE_VERTICAL_ADAPTER, getActivity().getApplication());
        View emptyView = getLayoutInflater().inflate(R.layout.favorites_empty_layout, recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, null);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        recyclerView.showShimmerAdapter();
        placeViewModel = ViewModelProviders.of(this, placeVMFactory)
                .get(PlaceViewModel.class);

        observer = new Observer<PlacesWrapper>() {
            @Override
            public void onChanged(@Nullable PlacesWrapper placesWrapper) {
                recyclerView.hideShimmerAdapter();
                swipeRefreshLayout.setRefreshing(false);
                if (placesWrapper != null) {
                    List<Place> places = placesWrapper.getPlaces();
                    if (places != null && places.size() > 0) {
                        final List<PlaceModel> placeModels = PlaceModelMapper.transformAll(places);
                        placeDBRepository.emptyFavorites().observe(FavoritesFragment.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer longs) {
                                placeDBRepository.insertPlaceFavorites(placeModels).observe(FavoritesFragment.this, new Observer<List<Long>>() {
                                    @Override
                                    public void onChanged(@Nullable List<Long> longs) {
                                        adapter.updateData(placeModels);
                                    }
                                });
                            }

                        });
                    } else {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                } else {
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                }
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFavoritesFromDatabase();
                if (ConnectivityUtils.isConnected(getContext())) {
                    String tokenId = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
                    placeViewModel.getMyFavorites(tokenId).observe(FavoritesFragment.this, observer);
                } else {
                    getFavoritesFromDatabase();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (ConnectivityUtils.isConnected(getContext())) {
            String tokenId = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
            placeViewModel.getMyFavorites(tokenId).observe(FavoritesFragment.this, observer);
        } else {
            getFavoritesFromDatabase();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    void getFavoritesFromDatabase() {
        swipeRefreshLayout.setRefreshing(false);
        placeDBRepository.getPlacesFavorite().observe(this, new Observer<List<PlaceModel>>() {
            @Override
            public void onChanged(@Nullable List<PlaceModel> placeModels) {
                if (placeModels != null) {
                    recyclerView.hideShimmerAdapter();
                    adapter.updateData(placeModels);
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                } else {
                    fetchFromAPI();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                deleteAllFavorites();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllFavorites() {
        placeDBRepository.emptyFavorites().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                getFavoritesFromDatabase();
            }
        });
    }

    void fetchFromAPI() {
        adapter.updateData(null);
        String userId = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
        placeViewModel.getMyFavorites(userId).observe(FavoritesFragment.this, observer);
    }

    @Override
    public void onPlaceClick(PlaceModel place) {
        Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PLACE_ID, place.get_id());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorites, menu);
    }

    @Override
    public void onStop() {
        super.onStop();
        placeDBRepository.onStop();
    }
}
