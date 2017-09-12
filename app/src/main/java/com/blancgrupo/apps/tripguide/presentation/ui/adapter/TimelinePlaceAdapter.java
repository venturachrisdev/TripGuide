package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceTypesCover;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/12/17.
 */

public class TimelinePlaceAdapter
        extends RecyclerView.Adapter<TimelinePlaceAdapter.TimelinePlaceViewHolder> {
    List<PlaceTypesCover> places;
    PlaceTimeLineListener lineListener;

    public TimelinePlaceAdapter(PlaceTimeLineListener lineListener) {
        this.lineListener = lineListener;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimelinePlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_place_item_layout, parent, false);
        return new TimelinePlaceViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimelinePlaceViewHolder holder, int position) {
        PlaceTypesCover place = places.get(position);
        holder.itemTitleText.setText(place.getName());
        holder.itemLocationText.setText(TextStringUtils.shortText(
                place.getAddress(), 35
        ));
        holder.setOnCardClickListener(lineListener, place);
        holder.setOnCardLongClickListener(lineListener, place, position);
//        holder.timelineView.setMarker(ContextCompat.getDrawable(
//                holder.itemView.getContext(),
//                R.drawable.ic_place_white_24dp
//        ), R.color.colorAccent);
    }

    @Override
    public int getItemCount() {
        if (places != null) {
            return places.size();
        }
        return 0;
    }

    public void updateData(List<PlaceTypesCover> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    class TimelinePlaceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.place_item_title)
        TextView itemTitleText;
        @BindView(R.id.place_item_location)
        TextView itemLocationText;
        @BindView(R.id.timeline_view)
        TimelineView timelineView;
        @BindView(R.id.cardview)
        CardView cardView;

        public TimelinePlaceViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timelineView.initLine(viewType);

        }

        public void setOnCardClickListener(final PlaceTimeLineListener listener,
                                           final PlaceTypesCover cover) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardPlaceClick(cover);
                }
            });
        }

        public void setOnCardLongClickListener(final PlaceTimeLineListener listener,
                                               final PlaceTypesCover cover, final int position) {
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    timelineView.setMarkerColor(R.color.colorIcon);
                    listener.onCardPlaceLongClick(cover, position);
                    return true;
                }
            });
        }
    }

    public interface PlaceTimeLineListener {
        void onCardPlaceClick(PlaceTypesCover cover);
        void onCardPlaceLongClick(PlaceTypesCover cover, int position);
    }
}
