package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TourAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourViewModel;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursFragment extends LifecycleFragment {
    @BindView(R.id.tours_rv)
    ShimmerRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    TourAdapter adapter;
    @Inject
    TourVMFactory tourVMFactory;
    TourViewModel tourViewModel;

    public ToursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tours, container, false);
        ButterKnife.bind(this, v);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getActivity().getApplication())
                .getNetComponent())
                .build()
                .inject(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new TourAdapter(getActivity().getApplication());
        View errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
        View emptyView = getLayoutInflater().inflate(R.layout.nothing_to_show_layout, recyclerView, false);
        ((TextView) emptyView.findViewById(R.id.textView)).setText(R.string.no_tours_yet);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectivityUtils.isConnected(getActivity().getApplicationContext())) {
                    recyclerView.showShimmerAdapter();
                    tourViewModel.loadTours();
                } else {
                    Toast.makeText(getContext(),
                            R.string.network_error, Toast.LENGTH_LONG)
                            .show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        tourViewModel = ViewModelProviders.of(this, tourVMFactory).get(TourViewModel.class);
        tourViewModel.getTours().observe(this, new Observer<PlacesWrapper>() {
            @Override
            public void onChanged(@Nullable PlacesWrapper placesWrapper) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                recyclerView.hideShimmerAdapter();
                if (placesWrapper != null) {
                    if (placesWrapper.getPlaces() != null && placesWrapper.getPlaces().size() > 0) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        adapter.updateData(placesWrapper.getPlaces());
                    } else {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                } else {
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                }
            }
        });
        return v;
    }

}
