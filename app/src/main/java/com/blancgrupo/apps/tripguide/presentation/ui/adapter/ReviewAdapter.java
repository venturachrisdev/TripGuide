package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by venturachrisdev on 9/22/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    List<Review> reviews;
    int type;
    public static final int REVIEW_PLACE_TYPE = 1200;
    public static final int REVIEW_PROFILE_TYPE = 1201;
    ReviewProfileListener profileListener;

    public ReviewAdapter(int type, ReviewProfileListener listener) {
        this.type = type;
        this.profileListener = listener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (type) {
            case REVIEW_PROFILE_TYPE:
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_profile, parent, false);
            return new ReviewProfileViewHolder(view);
            case REVIEW_PLACE_TYPE:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
                return new ReviewPlaceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.setDate(review.getCreatedAt());
        holder.setMessage(review.getMessage());
        holder.setRating(review.getRating());

        switch (holder.getType()) {
            case REVIEW_PLACE_TYPE:
                ((ReviewPlaceViewHolder) holder).setName(review.getProfile().getName());
                ((ReviewPlaceViewHolder) holder).setPhoto(review.getProfile().getPhotoUrl());
                break;
            case REVIEW_PROFILE_TYPE:
                ((ReviewProfileViewHolder) holder).setPlaceName(review.getPlace().getName());
                ((ReviewProfileViewHolder) holder).setOnReviewPlaceClickListener(
                        profileListener, review.getPlace());
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (reviews != null) {
            return reviews.size();
        }
        return 0;
    }

    public void updateData(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_profile_date)
        TextView profileDate;
        @BindView(R.id.review_profile_message)
        TextView profileMessage;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        int type;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setDate(String date) {
            try {
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                Date result = df1.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result);
                profileDate.setText(SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                        DateFormat.SHORT)
                        .format(calendar.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
                profileDate.setText(date);
            }
        }

        public void setMessage(String message) {
            profileMessage.setText(message);
        }

        public void setRating(double rating) {
            ratingBar.setRating((float) rating);
        }

        public int getType() {
            return this.type;
        }
    }

    public class ReviewPlaceViewHolder extends ReviewViewHolder {
        @BindView(R.id.review_profile_photo)
        CircleImageView profilePhoto;
        @BindView(R.id.review_profile_name)
        TextView profileName;

        public ReviewPlaceViewHolder(View itemView) {
            super(itemView);
            this.type = REVIEW_PLACE_TYPE;
        }

        public void setName(String name) {
            profileName.setText(name);
        }

        public void setPhoto(String photoUrl) {
            Glide.with(itemView.getContext())
                    .load(photoUrl)
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.mipmap.profile_placeholder)
                    .into(profilePhoto);
        }
    }

    public class ReviewProfileViewHolder extends ReviewViewHolder {
        @BindView(R.id.review_place_name)
        TextView placeName;

        public ReviewProfileViewHolder(View itemView) {
            super(itemView);
            this.type = REVIEW_PROFILE_TYPE;
        }

        public void setPlaceName(String placeName) {
            this.placeName.setText(placeName);
        }

        public void setOnReviewPlaceClickListener(final ReviewProfileListener listener,
                                                  final Review.ReviewPlace reviewPlace) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onReviewProfileClick(reviewPlace);
                }
            });
        }
    }

    public interface ReviewProfileListener {
        void onReviewProfileClick(Review.ReviewPlace reviewPlace);
    }
}
