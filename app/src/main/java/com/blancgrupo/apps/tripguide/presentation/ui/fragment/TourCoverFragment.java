package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.TourCover;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.TourActivity;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourCoverFragment extends Fragment {
    @BindView(R.id.tour_type_header_image)
    LoaderImageView imageView;
    @BindView(R.id.tour_type_title_text)
    TextView tourNameText;
    @BindView(R.id.tour_type_time_text)
    TextView tourTimeText;
    @BindView(R.id.tour_type_distance_text)
    TextView tourDistanceText;


    public TourCoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tour_cover, container, false);
        ButterKnife.bind(this, v);
        Bundle args = getArguments();
        TourCover cover = args.getParcelable(Constants.EXTRA_SINGLE_TOUR_ID);
        String pic_url = args.getString(Constants.EXTRA_IMAGE_URL);
        if (pic_url != null) {
            Glide.with(getContext())
                    .load(pic_url)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }
        if (cover != null) {
            tourNameText.setText(cover.getName());
            tourTimeText.setText(String.format("%d min", cover.getTotalTime()));
            tourDistanceText.setText(String.format("%dm", cover.getTotalDistance()));
        }
        return v;
    }

}
