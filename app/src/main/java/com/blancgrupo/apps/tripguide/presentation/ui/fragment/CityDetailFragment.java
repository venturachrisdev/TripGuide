package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Topic;
import com.blancgrupo.apps.tripguide.domain.model.PlaceModel;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.ChooseLocationActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.CityToursActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.DisplayImageActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.PlaceDetailActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.SearchActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.TourActivity;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.TopicAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.custom.FeatureCardView;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.CityViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityDetailFragment extends Fragment
        implements LifecycleRegistryOwner, TextStringUtils.PlaceItemActivityListener, PlaceAdapter.PlaceAdapterListener,
        TopicAdapter.TopicListener, LocationListener {
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    CityViewModel cityViewModel;
    @Inject
    CityVMFactory cityVMFactory;
    @Inject
    SharedPreferences sharedPreferences;
    Observer<CityWrapper> observer;


    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.city_name)
    TextView cityNameText;
    @BindView(R.id.featurecardview)
    FeatureCardView featureCardView;
    @BindView(R.id.all_topics_rv)
    ShimmerRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    TopicAdapter adapter;
    View errorView;
    String  cityId;

    public CityDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_city_detail, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        adapter = new TopicAdapter(this, this, getActivity().getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        View emptyView = getLayoutInflater().inflate(R.layout.nothing_to_show_layout, recyclerView, false);
        errorView = getLayoutInflater().inflate(R.layout.no_internet_layout, recyclerView, false);
        ((TextView) errorView.findViewById(R.id.textView2)).setText(R.string.network_error_tap_to_retry);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, errorView);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        recyclerView.showShimmerAdapter();

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getActivity().getApplication()).getNetComponent())
                .build()
                .inject(this);

        Bundle data = getArguments();
        if (data != null && data.containsKey(Constants.EXTRA_CITY_ID) && data.getString(Constants.EXTRA_CITY_ID) != null) {
            cityId = data.getString(Constants.EXTRA_CITY_ID);

        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LocationUtils.checkForPermission(getActivity())) {
            LocationUtils.requestLocationUpdates(getActivity().getApplicationContext(), this);
        }
        if (cityId == null) {
            if (!sharedPreferences.contains(Constants.CURRENT_LOCATION_SP)) {
                Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
                startActivityForResult(intent, Constants.CHOOSE_LOCATION_RC);
            } else {
                cityId = sharedPreferences.getString(Constants.CURRENT_LOCATION_SP, null);
            }
        }
        cityViewModel = ViewModelProviders.of(this, cityVMFactory)
                .get(CityViewModel.class);

        observer = new Observer<CityWrapper>() {
            @Override
            public void onChanged(@Nullable CityWrapper cityWrapper) {
                // STOP PROGRESS
                recyclerView.hideShimmerAdapter();
                swipeRefreshLayout.setRefreshing(false);
                if (cityWrapper != null) {
                    if (cityWrapper.getCity() != null) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        bindCity(cityWrapper.getCity());
                    } else {
                        if (!ConnectivityUtils.isConnected(getActivity().getApplicationContext())) {
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                        } else {
                            Toast.makeText(getContext(), cityWrapper.getStatus(), Toast.LENGTH_SHORT).show();
                            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                        }
                    }
                } else {
                    statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                }
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cityViewModel.loadSingleCity(cityId);
                recyclerView.showShimmerAdapter();
            }
        });

        if (cityViewModel.isSingleCityLoaded()) {
            cityViewModel.getLoadedSingleCity().observe(this, observer);
        } else {
            cityViewModel.getSingleCity(cityId).observe(this, observer);
        }

        if (errorView != null) {
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cityViewModel != null) {
                        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                        recyclerView.showShimmerAdapter();
                        String id = sharedPreferences.getString(Constants.CURRENT_LOCATION_SP, null);
                        cityViewModel.loadSingleCity(id);
                    }
                }
            });
        }
    }


    void bindCity(City city) {
        cityNameText.setText(city.getName());
        recyclerView.hideShimmerAdapter();
//        featureCardView.show();
        Photo header = city.getPhoto();

        if (header != null && header.getReference() != null) {
            final List<Photo> photos = new ArrayList<>();
            photos.add(header);
            String url = ApiUtils.getPlacePhotoUrl((MyApplication) getActivity().getApplication(),
                    header.getReference(), header.getWidth());
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into(headerImage);

            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DisplayImageActivity.class);
                    intent.putParcelableArrayListExtra(Constants.EXTRA_IMAGE_URL,
                            (ArrayList<? extends Parcelable>) photos);
                    startActivity(intent);
                }
            });
        }
        // BIND TOPICS
        List<Topic> topics = city.getTopics();
        if (topics.size() > 0) {
//            if (topics.get(0).get_id().equals("tour")) {
//                featureCardView.hide();
//            } else {
//                featureCardView.show();
//            }
            adapter.updateData(topics);
        } else {
            Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
            intent.putExtra(Constants.EXTRA_PLACE_GOOGLE_ID, city.getGoogleId());
            startActivity(intent);
            getActivity().finish();
        }

    }

    @Override
    public void onPlaceClick(PlaceModel place) {
        if (!place.getType().equals("tour")) {
            Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
            intent.putExtra(Constants.EXTRA_PLACE_ID, place.get_id());
            startActivity(intent);
        } else {
            // A Tour
            Intent intent = new Intent(getActivity(), TourActivity.class);
            intent.putExtra(Constants.EXTRA_PLACE_TOUR_ID, place.get_id());
            startActivity(intent);
        }

    }

    @Override
    public void onMoreButtonClick(String topicTitle, boolean isTour) {
        if (isTour) {
            Intent intent = new Intent(getActivity(), CityToursActivity.class);
            intent.putExtra(Constants.EXTRA_CITY_ID, cityId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            String topify = TextStringUtils.formatTitle(topicTitle);
            intent.putExtra(Constants.EXTRA_SEARCH_PLACE_QUERY, topify + " " +
                    getString(R.string.in) + " " + cityNameText.getText());
            startActivity(intent);
        }
    }

    @Override
    public void onTourPresence() {
//        featureCardView.hide();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
