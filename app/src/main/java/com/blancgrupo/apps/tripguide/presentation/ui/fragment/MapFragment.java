package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.Manifest;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.SearchVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.SearchViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends LifecycleFragment implements OnMapReadyCallback {
    private GoogleMap map;
    SearchViewModel searchViewModel;

    @Inject
    SearchVMFactory searchVMFactory;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getActivity().getApplication())
                        .getNetComponent())
                .build()
                .inject(this);
        searchViewModel = ViewModelProviders.of(this, searchVMFactory)
                .get(SearchViewModel.class);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (map == null) {
            final OnMapReadyCallback callback = this;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SupportMapFragment mapFragment = new SupportMapFragment();
                    mapFragment.getMapAsync(callback);
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, mapFragment)
                            .commit();
                }
            }, 100);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setIndoorEnabled(true);
        map.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.EXTRA_PLACE_FOR_MAP)) {
            Place place = args.getParcelable(Constants.EXTRA_PLACE_FOR_MAP);
                if (place != null) {
                    mapPlace(place);
                }
        } else {
            // we need to get the current location.
        }
    }

    private void mapPlace(@NonNull Place place) {
        Location placeLocation = place.getLocation();
        LatLng where = new LatLng(placeLocation.getLat(), placeLocation.getLng());
        map.addMarker(new MarkerOptions()
                .position(where)
                .title(place.getName())
                .icon(ApiUtils.drawMarkerByType(getActivity().getApplicationContext(),
                        place.getTypes().get(0))));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(where, 17f));
    }

}
