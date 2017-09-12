package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.TourCover;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.TourActivity;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
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
    @BindView(R.id.tour_type_places_text)
    TextView tourPlacesText;
    @BindView(R.id.btn)
    Button btn;
    ApiUtils.SingleTourListener listener;
    String imageUrl;


    public TourCoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ApiUtils.SingleTourListener) {
            listener = (ApiUtils.SingleTourListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SingleTourListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tour_cover, container, false);
        ButterKnife.bind(this, v);
        Bundle args = getArguments();
        final TourCover cover = args.getParcelable(Constants.EXTRA_SINGLE_TOUR_ID);
        String pic_url = args.getString(Constants.EXTRA_IMAGE_URL);
        imageUrl = pic_url;
        if (pic_url != null) {
            Glide.with(getContext())
                    .load(pic_url)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }
        if (cover != null) {
            tourNameText.setText(cover.getName());
            tourTimeText.setText(ApiUtils.getTourTime(getContext(), cover.getTotalTime()));
            tourDistanceText.setText(ApiUtils.getTourDistance(getContext(), cover.getTotalDistance()));
            if (cover.getPlaces() != null && cover.getPlaces().size() > 0) {
                tourPlacesText.setText(String.valueOf(cover.getPlaces().size()));
            } else {
                tourPlacesText.setText(R.string.n_a);
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSingleTourClick(cover, imageUrl);
            }
        });
        return v;
    }


}
