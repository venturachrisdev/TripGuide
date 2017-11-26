package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Country;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by venturachrisdev on 11/25/17.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    List<Country> countries;
    private MyApplication application;
    CountryAdapterListener listener;

    public interface CountryAdapterListener {
        void onCountryItemClick(Country country);
    }

    public CountryAdapter(MyApplication application, CountryAdapterListener listener) {
        this.application = application;
        this.listener = listener;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        return new CountryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.setName(country.getName());
        holder.setParent(country.getContinent());
        holder.setOnClickListener(listener, country);
        holder.setPhoto(application, country.getPhoto().getReference(),
                country.getPhoto().getWidth());
    }

    public void updateData(List<Country> newCountries) {
        this.countries = newCountries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (countries != null) {
            return countries.size();
        }
        return 0;
    }


    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name)
        LoaderTextView cityName;
        @BindView(R.id.city_image)
        ImageView cityImage;
        @BindView(R.id.city_parent)
        LoaderTextView cityParent;

        public CountryViewHolder(View itemView) {
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

        public void setOnClickListener(final CountryAdapterListener listener, final Country country) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCountryItemClick(country);
                }
            });
        }
    }
}
