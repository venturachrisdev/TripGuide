package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 8/21/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<PlaceCover> places;
    private int adapterType;
    public static final int PLACE_VERTICAL_ADAPTER = 100;
    public static final int PLACE_HORIZONTAL_ADAPTER = 101;
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
        int id;
        switch (adapterType) {
            case PLACE_HORIZONTAL_ADAPTER:
                id = R.layout.grid_place_item;
                break;
            case PLACE_VERTICAL_ADAPTER:
            default:
                id = R.layout.place_item_list;
        }
        View root = inflater.inflate(id, parent, false);
        return new PlaceViewHolder(root, adapterType);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        PlaceCover place = places.get(position);
        holder.setOnClickListener(listener, place);
        holder.setType(TextStringUtils.formatTitle(place.getType()));
        holder.setName(place.getName());
        holder.setLocation(place.getAddress());
        holder.setDistance(app, place.getLocation());
        Photo photo = place.getPhoto();
        if (photo != null && photo.getReference() != null) {
            int width = adapterType == PLACE_VERTICAL_ADAPTER ? 200 : 700;
            holder.setImage(ApiUtils.getPlacePhotoUrl(app, photo.getReference(), width));
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

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        int adapterType;
        ImageView image;
        LoaderTextView nameText;
        LoaderTextView locationText;
        LoaderTextView distanceText;

        public PlaceViewHolder(View itemView) {
            super(itemView);
        }

        public PlaceViewHolder(View itemView, int adapterType) {
            super(itemView);
            this.adapterType = adapterType;
            nameText = itemView.findViewById(R.id.place_item_title);
            locationText = itemView.findViewById(R.id.place_item_location);
        }

        public void setImage(String imageUrl) {
            switch (adapterType) {
                case PLACE_VERTICAL_ADAPTER:
                    image = itemView.findViewById(R.id.place_item_photo);
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .placeholder(R.mipmap.place_holder)
                            .centerCrop()
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(image);
                    break;
                case PLACE_HORIZONTAL_ADAPTER:
                    image = itemView.findViewById(R.id.place_item_photo);
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .centerCrop()
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(image);
                    break;
            }
        }


        public void setType(String type) {
            TextView typeText =  itemView.findViewById(R.id.place_item_types);
            typeText.setText(type);
        }


        public void setLocation(String location) {
            locationText.setText(location);
        }


        public void setOnClickListener(final PlaceAdapterListener listener, final PlaceCover place) {
            if (!place.getType().equals("tour")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onPlaceClick(place);
                    }
                });
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PlaceAdapter.this.app, "Tours can wait!", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }

        public void setName(String name) {
            this.nameText.setText(name);
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
}
