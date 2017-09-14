package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.presentation.ui.service.LocationService;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.CompletableOnSubscribe;

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
    PlaceTypesCover cover;
    double startDistance = 0;
    double realDistance = 0;
    ApiUtils.RunningPlaceListener listener;
    private String tourId;

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
        tourId = args.getString(Constants.EXTRA_SINGLE_TOUR_ID);
        if (place != null) {
            cover = place;
            bindPlace(place);
            LocationUtils.requestLocationUpdates(getContext(), this);
        }

        bindPosition(args.getInt(Constants.EXTRA_TOTAL), args.getInt(Constants.EXTRA_CURRENT_POSITION));
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
        Glide.with(getContext())
                .load(ApiUtils.getPlacePhotoUrl((MyApplication) getActivity().getApplication(),
                        place.getPhoto().getReference(), place.getPhoto().getWidth()))
                .centerCrop()
                .crossFade()
                .into(placePhoto);
        Location currentLocation = LocationUtils.getCurrentLocation(getContext());
        com.blancgrupo.apps.tripguide.data.entity.api.Location coverLocation = place.getLocation();
        if (currentLocation != null) {
            String distance = LocationUtils.measureDistance(getContext(), currentLocation,
                    coverLocation.getLat(), coverLocation.getLng());
            if (startDistance == 0) {
                startDistance = LocationUtils.measureDoubleDistance(getContext(), currentLocation,
                        coverLocation.getLat(), coverLocation.getLng());
            }
            if (realDistance == 0) {
                realDistance = startDistance;
            }
            placeDistance.setText(distance);
            calculateProgress();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent backgroundIntent = new Intent(getActivity(), LocationService.class);
        backgroundIntent.putExtra(Constants.EXTRA_PLACE_ID, cover);
        backgroundIntent.putExtra(Constants.EXTRA_CURRENT_POSITION, startDistance);
        backgroundIntent.putExtra(Constants.EXTRA_CURRENT_IMAGE_POSITION, String.valueOf(currentPosition.getText().charAt(0)));
        backgroundIntent.putExtra(Constants.EXTRA_SINGLE_TOUR_ID, tourId);
        Toast.makeText(getContext(), "Tour ID desde fragment: " + tourId, Toast.LENGTH_SHORT).show();
        getActivity().startService(backgroundIntent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.running_place, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(cover.getName())
//                    .setContentText("Distance: " + LocationUtils.prettifyDistance(realDistance));
//            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (cover != null) {
            com.blancgrupo.apps.tripguide.data.entity.api.Location coverLocation = cover.getLocation();
            String distance = LocationUtils.measureDistance(getContext(), location,
                    coverLocation.getLat(), coverLocation.getLng());
            realDistance = LocationUtils.measureDoubleDistance(getContext(), location,
                    coverLocation.getLat(), coverLocation.getLng());
            if (startDistance == 0) {
                startDistance = realDistance;
            }
            placeDistance.setText(distance);

        }
    }

    void calculateProgress() {
        int percent = (int) (100 / (startDistance / realDistance));
        int progress = 100 - percent;
        if (progress >= 0) {
            progressBar.setProgress(progress);
        } else {
            progressBar.setProgress(0);
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
