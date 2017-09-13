package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunningPlaceFragment extends Fragment implements LocationListener {
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.place_item_title)
    TextView placeName;
    @BindView(R.id.place_item_location)
    TextView placeLocation;
    @BindView(R.id.place_item_distance)
    TextView placeDistance;
    @BindView(R.id.btn)
    Button placeBtn;
    PlaceTypesCover cover;

    public RunningPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_running_place, container, false);
        ButterKnife.bind(this, v);
        progressBar.setProgress(50);

        Bundle args = getArguments();
        PlaceTypesCover place = args.getParcelable(Constants.EXTRA_PLACE_ID);
        if (place != null) {
            cover = place;
            bindPlace(place);
            LocationUtils.requestLocationUpdates(getContext(), this);
        }

        return v;
    }

    private void bindPlace(PlaceTypesCover place) {
        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        placeName.setText(place.getName());
        placeLocation.setText(TextStringUtils.shortText(place.getAddress(), 50));
        Location currentLocation = LocationUtils.getCurrentLocation(getContext());
        com.blancgrupo.apps.tripguide.data.entity.api.Location coverLocation = place.getLocation();
        if (currentLocation != null) {
            String distance = LocationUtils.measureDistance(getContext(), currentLocation, coverLocation.getLat(), coverLocation.getLng());
            placeDistance.setText(distance);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (cover != null) {
            com.blancgrupo.apps.tripguide.data.entity.api.Location coverLocation = cover.getLocation();
            String distance = LocationUtils.measureDistance(getContext(), location, coverLocation.getLat(), coverLocation.getLng());
            placeDistance.setText(distance);
        }
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
