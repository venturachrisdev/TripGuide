package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 8/30/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    Application app;
    List<Photo> photos;
    PhotoListener photoListener;

    public interface PhotoListener {
        void onPhotoListener(Photo photo, int position);
    }

    public PhotoAdapter(Application app, PhotoListener listener, List<Photo> photos) {
        this.app = app;
        this.photoListener = listener;
        this.photos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_grid, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.setImageUrl(app, photo.getReference(), photo.getWidth() / 3);
        holder.setOnClickListener(photoListener, photo, position);
    }

    @Override
    public int getItemCount() {
        if (photos != null) {
            return photos.size();
        }
        return 0;
    }

    public void updateData(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        ImageView photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImageUrl(Application app, String reference,
                                int width) {
            Glide.with(itemView.getContext())
                .load(ApiUtils.getPlacePhotoUrl((MyApplication) app,
                        reference, width))
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo);
        }

        public void setOnClickListener(final PhotoListener photoListener, final Photo photo, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoListener.onPhotoListener(photo, position);
                }
            });
        }
    }
}
