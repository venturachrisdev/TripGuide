package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.RunningTourActivity;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

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
    @BindView(R.id.place_item_photo)
    CircleImageView placePhoto;
    @BindView(R.id.current_page)
    TextView currentPosition;
    @BindView(R.id.btn_navigate)
    Button btnNavigate;
    int position = 0;
    PlaceTypesCover cover;
    double realDistance = 0;
    int progress;
    double startDistance = 0;
    ApiUtils.RunningPlaceListener listener;
    private int requestCode = 898;

    public RunningPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ApiUtils.RunningPlaceListener) {
            listener = (ApiUtils.RunningPlaceListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement RunningPlaceListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_running_place, container, false);
        ButterKnife.bind(this, v);
        progressBar.setProgress(0);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        PlaceTypesCover place = args.getParcelable(Constants.EXTRA_PLACE_ID);
        position = args.getInt(Constants.EXTRA_CURRENT_POSITION);
        if (realDistance == 0) {
            realDistance = args.getDouble(Constants.EXTRA_CURRENT_DISTANCE);
        }
        if (progress == 0) {
            progress = args.getInt(Constants.EXTRA_PROGRESS);
        }
        if (startDistance == 0) {
            startDistance = args.getDouble(Constants.EXTRA_START_POSITION);
        }
        bindProgress(realDistance, progress);
        if (place != null) {
            cover = place;
            bindPlace(place);
        }

        bindPosition(args.getInt(Constants.EXTRA_TOTAL), position);
        return v;
    }

    private void bindPosition(int total, int current) {
        String str = String.format(Locale.getDefault(), "%d %s %d", current, getString(R.string.of), total);
        currentPosition.setText(str);
    }

    private void bindPlace(final PlaceTypesCover place) {
        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRunningPlaceClick(place);
            }
        });
        placeName.setText(place.getName());
        placeLocation.setText(TextStringUtils.shortText(place.getAddress(), 50));
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = String.format("%s,%s", place.getLocation().getLat(),
                        place.getLocation().getLng());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + location));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, requestCode);
                } else {
                    Toast.makeText(getContext(), R.string.please_install_google_maps, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Glide.with(getContext())
                .load(ApiUtils.getPlacePhotoUrl((MyApplication) getActivity().getApplication(),
                        place.getPhoto().getReference(), place.getPhoto().getWidth()))
                .centerCrop()
                .crossFade()
                .into(placePhoto);

        startLocationService(place.getLocation());
    }

    private void startLocationService(Location location) {
        android.location.Location whereIam = LocationUtils.getCurrentLocation(getContext());
        if (startDistance <= 0) {
            startDistance = LocationUtils.measureDoubleDistance(getContext(), whereIam, cover.getLocation().getLat(), cover.getLocation().getLng());
        }
        LocationUtils.requestLocationUpdates(getContext(), this);
        if (whereIam != null) {
            showDistance(whereIam, location);
        }
    }

    private void showDistance(android.location.Location whereIam, Location location) {
        double distance = LocationUtils.measureDoubleDistance(getContext(), whereIam, location.getLat(), location.getLng());
        realDistance = distance;
        int percent = (int) (100 / (startDistance / realDistance));
        progress = 100 - percent;
        Log.d("TRIPGUIDE_TOUR", "startDistance: " + startDistance);
        Log.d("TRIPGUIDE_TOUR", "realDistance: " + realDistance);
        Log.d("TRIPGUIDE_TOUR", "Progress: " + progress);
        if (progress >= 0) {
            bindProgress(distance, progress);
            if (progress > 96 || distance < 100) {
                ((RunningTourActivity) getActivity()).startPlaceDetail();
            }
        } else {
            bindProgress(distance, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Go with next place", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Not completed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.running_place, menu);
    }

    public void bindProgress(double distance, int progress) {
        if (distance == 0) {
            placeDistance.setText(LocationUtils.prettifyDistance(startDistance));
        } else {
            placeDistance.setText(LocationUtils.prettifyDistance(distance));
        }
        progressBar.setProgress(progress);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (isAdded()) {
            showDistance(location, cover.getLocation());
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
