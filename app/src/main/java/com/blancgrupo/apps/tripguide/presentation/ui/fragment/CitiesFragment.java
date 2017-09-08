package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.ChooseLocationActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.CityAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.custom.ExploreMyCityView;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesFragment extends LifecycleFragment implements CityAdapter.CityAdapterListener {
    @BindView(R.id.cities_rv)
    ShimmerRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.explore_my_cityview)
    ExploreMyCityView exploreMyCityView;
    CityViewModel cityViewModel;
    CitiesFragmentListener listener;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;

    CityAdapter adapter;

    @Inject
    CityVMFactory cityVMFactory;

    public CitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cities, container, false);
        ButterKnife.bind(this, v);
        DaggerActivityComponent.builder()
                .netComponent(((MyApplication) getActivity().getApplication())
                        .getNetComponent())
                .build()
                .inject(this);

        adapter = new CityAdapter((MyApplication) getActivity().getApplication(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        View errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
        ((TextView) errorView.findViewById(R.id.textView2)).setText(R.string.we_could_not_establish_connection_scroll);
        View emptyView = getLayoutInflater().inflate(R.layout.nothing_to_show_layout, recyclerView, false);
        statesRecyclerViewAdapter =
                new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        cityViewModel = ViewModelProviders.of(getActivity(), cityVMFactory)
                .get(CityViewModel.class);

        Observer<CitiesWrapper> observer = new Observer<CitiesWrapper>() {
            @Override
            public void onChanged(@Nullable CitiesWrapper citiesWrapper) {
                recyclerView.hideShimmerAdapter();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                // STOP PROGRESS
                if (citiesWrapper != null && citiesWrapper.getStatus().equals(Constants.STATUS_OK)) {
                    if (citiesWrapper.getCities() != null && citiesWrapper.getCities().size() > 0) {
                        displayContent();
                        adapter.updateData(citiesWrapper.getCities());
                    } else {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                } else {
                    if (!ConnectivityUtils.isConnected(getActivity().getApplicationContext())) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                    } else {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                }
            }
        };
        cityViewModel.getCities().observe(this, observer);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectivityUtils.isConnected(getActivity().getApplicationContext())) {
                    recyclerView.showShimmerAdapter();
                    cityViewModel.loadCities();
                } else {
                    Toast.makeText(getContext(),
                            R.string.network_error, Toast.LENGTH_LONG)
                            .show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        exploreMyCityView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityUtils.isConnected(getActivity().getApplicationContext())) {
                    goForCurrentCity();
                } else {
                    Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        displayNoInternet();
        recyclerView.showShimmerAdapter();
        return v;
    }

    void goForCurrentCity() {
        ((ChooseLocationActivity) getActivity()).goForCurrentLocation();
    }

    private void displayError(String status) {
        Log.d("Application", status);
        displayNoInternet();
    }

    void displayNoInternet() {
        if (!ConnectivityUtils.isConnected(getActivity().getApplicationContext())) {
            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
        }
    }

    void displayContent() {
        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CitiesFragmentListener) {
            listener = (CitiesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CitiesFragmentListener");
        }
    }

    @Override
    public void onCityItemClick(City city) {
        listener.onCityClick(city);
    }

    public void fetchCurrentCity(final ApiUtils.ActionCallback callback, String lat, String lng) {
        cityViewModel.getCurrentCityId(lat, lng)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        callback.call();
                        listener.onCityClick(new City(s));
                    }
                });
    }

    public interface CitiesFragmentListener {
        void onCityClick(City city);
    }

}
