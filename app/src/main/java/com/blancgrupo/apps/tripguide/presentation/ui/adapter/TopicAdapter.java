package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.app.Application;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
            holder.setTopicTitle(TextStringUtils.formatTitle(topic.get_id()) + "s");
        } else {
            holder.setTopicTitle(TextStringUtils.formatTitle(topic.get_id()));
        }
        holder.setupRecyclerView(topic.getPlaces(), placesListener, 100);
        holder.setTopicListener(topicListener, topic.get_id(), topic.get_id());
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

        public void setTopicTitle(String title) {
            topicTitle.setText(title);
        }


        public void setupRecyclerView(List<PlaceCover> places, PlaceAdapter.PlaceAdapterListener placesListener, int type) {
            LinearLayoutManager manager = new LinearLayoutManager(TopicAdapter.this.app, LinearLayoutManager.HORIZONTAL, false);
            topicRecyclerView.setLayoutManager(manager);
            PlaceAdapter adapter = new PlaceAdapter(placesListener, PlaceAdapter.PLACE_HORIZONTAL_ADAPTER, TopicAdapter.this.app);
            topicRecyclerView.setAdapter(adapter);
            adapter.updateData(places);
        }

        public void setTopicListener(final TopicListener listener, final String topicTitle, String title) {
            if (!title.equals(Constants.HIGHLIGHT_CATEGORY)) {
                moreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onMoreButtonClick(topicTitle);
                    }
                });
            } else {
                moreBtn.setVisibility(View.GONE);
            }
        }

        public void setDarkStyle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.setBackgroundColor(TopicAdapter.this.app.getColor(R.color.colorPrimaryDark));
                topicTitle.setTextColor(TopicAdapter.this.app.getColor(R.color.colorIcon));
            }
        }
    }

    public interface TopicListener {
        void onMoreButtonClick(String topicTitle);
    }
}
