package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;

import java.util.List;

/**
 * Created by root on 8/29/17.
 */

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    List<Place> places;

    @Override
    public TourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_place_item, parent, false);
        return new TourViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (places != null) {
            return places.size();
        }
        return 0;
    }

    void updateData(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    class TourViewHolder extends RecyclerView.ViewHolder {

        public TourViewHolder(View itemView) {
            super(itemView);
        }
    }
}
