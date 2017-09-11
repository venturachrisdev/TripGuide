package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Location;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 8/29/17.
 */

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    List<Place> places;
    Application app;
    TourListener listener;

    public interface TourListener {
        void onTourClick(Place tour);
    }

    public TourAdapter(TourListener tourListener, Application app) {
        this.app = app;
        this.listener = tourListener;
    }

    @Override
    public TourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_item_layout, parent, false);
        return new TourViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TourViewHolder holder, int position) {
        Place place = places.get(position);
        holder.setName(place.getName());
        if (place.getCity() != null) {
            holder.setLocation(place.getCity().getName());
        }
        Photo photo = place.getPhoto();
        if (photo != null && photo.getReference() != null) {
            int width = photo.getWidth();
            holder.setImage(ApiUtils.getPlacePhotoUrl((MyApplication) app, photo.getReference(), width));
        }
        holder.setOnClickListener(listener, place);
    }

    @Override
    public int getItemCount() {
        if (places != null) {
            return places.size();
        }
        return 0;
    }

    public void updateData(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    class TourViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.place_item_title)
        LoaderTextView nameText;
        @BindView(R.id.place_item_photo)
        ImageView image;
        @BindView(R.id.place_item_location)
        LoaderTextView locationText;
        public TourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setLocation(String location) {
            this.locationText.setText(location);
        }

        public void setName(String name) {
            this.nameText.setText(name);
        }

        public void setImage(String imageUrl) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }

        public void setOnClickListener(final TourListener listener, final Place place) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTourClick(place);
                }
            });
        }
    }
}
