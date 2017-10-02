package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 8/21/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<PlaceCover> places;
    private int adapterType;
    public static final int PLACE_VERTICAL_ADAPTER      = 100;
    public static final int PLACE_HORIZONTAL_ADAPTER    = 101;
    public static final int PLACE_TOUR_ADAPTER          = 110;
    MyApplication app;
    PlaceAdapterListener listener;

    public interface PlaceAdapterListener {
        void onPlaceClick(PlaceCover place);
    }


    public PlaceAdapter(PlaceAdapterListener listener, int type, Application app) {
        this.listener = listener;
        adapterType = type;
        this.app = (MyApplication) app;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root;
        switch (adapterType) {
            case PLACE_HORIZONTAL_ADAPTER:
                root = inflater.inflate(R.layout.grid_place_item, parent, false);
                return new HorizontalPlaceViewHolder(root);
            case PLACE_TOUR_ADAPTER:
                root = inflater.inflate(R.layout.tour_item_layout, parent, false);
                return new TourPlaceViewHolder(root);
            case PLACE_VERTICAL_ADAPTER:
            default:
                root = inflater.inflate(R.layout.place_item_list, parent, false);
                return new VerticalPlaceViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        PlaceCover place = places.get(position);
        holder.setOnClickListener(listener, place);
        holder.setName(place.getName());
        holder.setLocation(place.getAddress());
        Photo photo = place.getPhoto();
        if (photo != null && photo.getReference() != null) {
            int width = adapterType == PLACE_VERTICAL_ADAPTER ? 200 : 700;
            holder.setImage(ApiUtils.getPlacePhotoUrl(app, photo.getReference(), width));
        }

        switch (holder.getViewHolderType()) {
            case PlaceViewHolder.PLACE_LIST:
                ((VerticalPlaceViewHolder) holder).setDistance(app, place.getLocation());
                ((VerticalPlaceViewHolder) holder).setType(TextStringUtils.formatTitle(place.getType()));
                break;
            case PlaceViewHolder.PLACE_GRID:
                ((HorizontalPlaceViewHolder) holder).setDistance(app, place.getLocation());
                ((HorizontalPlaceViewHolder) holder).setRating(place.getRating());
                break;
            case PlaceViewHolder.PLACE_TOUR:
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (places != null) {
            return places.size();
        }
        return 0;
    }


    public void updateData(List<PlaceCover> newplaces) {
        places = newplaces;
        notifyDataSetChanged();
    }

    abstract class PlaceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.place_item_photo)
        ImageView image;
        @BindView(R.id.place_item_title)
        LoaderTextView nameText;
        @BindView(R.id.place_item_location)
        LoaderTextView locationText;
        int holderType;
        public static final int PLACE_LIST = 100;
        public static final int PLACE_GRID = 101;
        public static final int PLACE_TOUR = 110;


        public PlaceViewHolder(View itemView) {
            super(itemView);
        }


        public void setOnClickListener(final PlaceAdapterListener listener, final PlaceCover place) {
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    listener.onPlaceClick(place);
                }
            });
        }

        public void setName(String name) {
            nameText.setText(name);
        }

        public abstract void setImage(String placePhotoUrl);

        public void setLocation(String address) {
            locationText.setText(address);
        }

        public int getViewHolderType() {
            return this.holderType;
        }
    }

    class HorizontalPlaceViewHolder extends PlaceViewHolder {
        @BindView(R.id.place_item_distance)
        LoaderTextView distanceText;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;

        public HorizontalPlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.holderType = PlaceViewHolder.PLACE_GRID;
        }

        public void setRating(double rating) {
            ratingBar.setRating((float) rating);
        }

        @Override
        public void setImage(String placePhotoUrl) {
            Glide.with(itemView.getContext())
                    .load(placePhotoUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }

        public void setDistance(MyApplication app, Location location) {
            distanceText = itemView.findViewById(R.id.place_item_distance);
            if (LocationUtils.checkForPermission(app)) {
                distanceText.setVisibility(View.VISIBLE);
                distanceText.setText(LocationUtils.measureDistance(
                        app,
                        LocationUtils.getCurrentLocation(app),
                        location.getLat(),
                        location.getLng()
                ));
            }
        }
    }

    class VerticalPlaceViewHolder extends PlaceViewHolder {
        @BindView(R.id.place_item_distance)
        LoaderTextView distanceText;
        @BindView(R.id.place_item_types)
        TextView typeText;

        public VerticalPlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.holderType = PlaceViewHolder.PLACE_LIST;
        }

        @Override
        public void setImage(String placePhotoUrl) {
            Glide.with(itemView.getContext())
                    .load(placePhotoUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }

        public void setDistance(MyApplication app, Location location) {
            distanceText = itemView.findViewById(R.id.place_item_distance);
            if (LocationUtils.checkForPermission(app)) {
                distanceText.setVisibility(View.VISIBLE);
                distanceText.setText(LocationUtils.measureDistance(
                        app,
                        LocationUtils.getCurrentLocation(app),
                        location.getLat(),
                        location.getLng()
                ));
            }
        }

        public void setType(String type) {
            typeText.setText(type);
        }
    }

    class TourPlaceViewHolder extends PlaceViewHolder {

        public TourPlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.holderType = PlaceViewHolder.PLACE_TOUR;
        }

        @Override
        public void setImage(String placePhotoUrl) {
            Glide.with(itemView.getContext())
                    .load(placePhotoUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }
    }
}
