package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

/**
 * Created by venturachrisdev on 11/24/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Region;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 8/18/17.
 */

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {
    private List<Region> regions;
    private MyApplication application;
    RegionAdapterListener listener;

    public interface RegionAdapterListener {
        void onRegionItemClick(Region region);
    }

    public RegionAdapter(MyApplication application, RegionAdapterListener listener) {
        this.application = application;
        this.listener = listener;
    }

    public RegionAdapter.RegionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        return new RegionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RegionViewHolder holder, int position) {
        Region region = regions.get(position);
        holder.setName(region.getName());
        holder.setPhoto(application, region.getPhoto().getReference(),
                1000);
        if (region.getCountry() != null) {
            holder.setParent(region.getCountry().getName());
            holder.setOnClickListener(listener, region);
        }
    }

    public void updateData(List<Region> newRegions) {
        this.regions = newRegions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (regions != null) {
            return regions.size();
        }
        return 0;
    }

    class RegionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name)
        LoaderTextView cityName;
        @BindView(R.id.city_image)
        ImageView cityImage;
        @BindView(R.id.city_parent)
        LoaderTextView cityParent;

        public RegionViewHolder(View itemView) {
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

        public void setOnClickListener(final RegionAdapterListener listener, final Region region) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRegionItemClick(region);
                }
            });
        }
    }
}

