package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 8/18/17.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<City> cities;
    private MyApplication application;
    CityAdapterListener listener;

    public interface CityAdapterListener {
        public void onCityItemClick(City city);
    }

    public CityAdapter(MyApplication application, CityAdapterListener listener) {
        this.application = application;
        this.listener = listener;
    }

    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        return new CityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = cities.get(position);
        holder.setName(city.getName());
        holder.setPhoto(application, city.getPhoto().getReference(),
                city.getPhoto().getWidth());
        holder.setParent(city.getParent());
        holder.setOnClickListener(listener, city);
    }

    public void updateData(List<City> newCities) {
        this.cities = newCities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (cities != null) {
            return cities.size();
        }
        return 0;
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name)
        LoaderTextView cityName;
        @BindView(R.id.city_image)
        ImageView cityImage;
        @BindView(R.id.city_parent)
        LoaderTextView cityParent;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setName(String name) {
            cityName.setText(name);
        }
        public void setPhoto(MyApplication app,  String reference, int width) {
            String url = ApiUtils.getPlacePhotoUrl(app, reference, width);

            Glide.with(itemView.getContext())
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cityImage);
        }

        public void setParent(String parent) {
            cityParent.setText(parent);
        }

        public void setOnClickListener(final CityAdapterListener listener, final City city) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCityItemClick(city);
                }
            });
        }
    }
}
