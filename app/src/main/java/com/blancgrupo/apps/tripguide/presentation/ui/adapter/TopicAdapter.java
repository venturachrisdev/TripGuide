package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.app.Application;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Place;
import com.blancgrupo.apps.tripguide.data.entity.api.PlaceCover;
import com.blancgrupo.apps.tripguide.data.entity.api.Topic;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 8/22/17.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    List<Topic> topics;
    PlaceAdapter.PlaceAdapterListener placesListener;
    Application app;
    TopicListener topicListener;


    public TopicAdapter(PlaceAdapter.PlaceAdapterListener placesListener, TopicListener topicListener, Application app) {
        this.placesListener = placesListener;
        this.topicListener = topicListener;
        this.app = app;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_layout, parent, false);
        return new TopicViewHolder(root);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        Topic topic = topics.get(position);
        if (topic.get_id().equals(Constants.HIGHLIGHT_CATEGORY)) {
            holder.setDarkStyle();
        }
        holder.setTopicIcon(topic.get_id());
        holder.setupRecyclerView(topic, placesListener);
        holder.setTopicListener(topicListener, topic.get_id());
    }

    @Override
    public int getItemCount() {
        if (topics != null) {
            return topics.size();
        }
        return 0;
    }

    public void updateData(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.topic_icon)
        ImageView icon;
        @BindView(R.id.topic_rv)
        ShimmerRecyclerView topicRecyclerView;
        @BindView(R.id.more_btn)
        Button moreBtn;
        @BindView(R.id.topic_title)
        LoaderTextView topicTitle;

        public TopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setupRecyclerView(Topic topic, PlaceAdapter.PlaceAdapterListener placesListener) {
            LinearLayoutManager manager = new LinearLayoutManager(TopicAdapter.this.app, LinearLayoutManager.HORIZONTAL, false);
            topicRecyclerView.setLayoutManager(manager);
            topicRecyclerView.setHasFixedSize(true);
            topicRecyclerView.addItemDecoration(new PaddingItemDecoration(32));
            int itemType = PlaceAdapter.PLACE_HORIZONTAL_ADAPTER;
            if (topic.get_id().equals("tour")) {
                itemType = PlaceAdapter.PLACE_TOUR_ADAPTER;
            }
            PlaceAdapter adapter = new PlaceAdapter(placesListener, itemType, TopicAdapter.this.app);
            topicRecyclerView.setAdapter(adapter);
            adapter.updateData(topic.getPlaces());
        }

        public void setTopicListener(final TopicListener listener, final String topicTitle) {
            if (!topicTitle.equals(Constants.HIGHLIGHT_CATEGORY)) {
                moreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onMoreButtonClick(TopicViewHolder.this.topicTitle.getText().toString());
                    }
                });
            } else {
                listener.onTourPresence();
                moreBtn.setVisibility(View.INVISIBLE);
            }
        }

        public void setDarkStyle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                itemView.setBackgroundColor(TopicAdapter.this.app.getColor(R.color.colorPrimaryDark));
                topicTitle.setTextColor(TopicAdapter.this.app.getColor(R.color.colorAccent));
                icon.setColorFilter(TopicAdapter.this.app.getColor(R.color.colorAccent));
            }
        }

        public void setTopicIcon(String id) {
            switch(id) {
                case "restaurant":
                    icon.setImageResource(R.drawable.ic_restaurant_black_24dp);
                    topicTitle.setText(R.string.restaurants);
                    break;
                case "beach":
                    icon.setImageResource(R.drawable.ic_beach_access_black_24dp);
                    topicTitle.setText(R.string.beaches);
                    break;
                case "hotel":
                    icon.setImageResource(R.drawable.ic_domain_black_24dp);
                    topicTitle.setText(R.string.hotels);
                    break;
                case "cafe":
                    icon.setImageResource(R.drawable.ic_local_cafe_black_24dp);
                    topicTitle.setText(R.string.cafes);
                    break;
                case "park":
                    icon.setImageResource(R.drawable.ic_nature_people_black_24dp);
                    topicTitle.setText(R.string.parks);
                    break;
                case "tour":
                    icon.setImageResource(R.drawable.ic_directions_walk_black_24dp);
                    topicTitle.setText(R.string.tours);
                    break;
                default:
                    topicTitle.setText(TextStringUtils.formatTitle(id));
            }
        }
    }

    public interface TopicListener {
        void onMoreButtonClick(String topicTitle);
        void onTourPresence();
    }

    class PaddingItemDecoration extends RecyclerView.ItemDecoration {
        private final int size;

        public PaddingItemDecoration(int size) {
            this.size = size;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            // Apply offset only to first item
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left += size;
            }
        }
    }
}
