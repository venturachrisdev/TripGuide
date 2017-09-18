package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment
        implements PlaceAdapter.PlaceAdapterListener {
    @BindView(R.id.favorites_rv)
    ShimmerRecyclerView recyclerView;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    PlaceAdapter adapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceAdapter(this, PlaceAdapter.PLACE_VERTICAL_ADAPTER, getActivity().getApplication());
        View emptyView = getLayoutInflater().inflate(R.layout.favorites_empty_layout, recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, null, emptyView, null);
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        return v;
    }

    @Override
    public void onPlaceClick(PlaceCover place) {

    }

}
