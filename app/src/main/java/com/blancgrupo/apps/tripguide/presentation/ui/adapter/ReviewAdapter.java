package com.blancgrupo.apps.tripguide.presentation.ui.adapter;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

import static com.blancgrupo.apps.tripguide.utils.Constants.API_UPLOAD_URL;

/**
 * Created by venturachrisdev on 9/22/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    List<ReviewModel> reviews;
    int type;
    public static final int REVIEW_PLACE_TYPE = 1200;
    public static final int REVIEW_PROFILE_TYPE = 1201;
    ReviewProfileListener profileListener;
    ReviewMenuListener reviewMenuListener;

    public ReviewAdapter(int type, ReviewProfileListener listener, ReviewMenuListener menuListener) {
        this.type = type;
        this.profileListener = listener;
        this.reviewMenuListener = menuListener;
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
        ReviewModel review = reviews.get(position);
        holder.setDate(review.getCreatedAt());
        holder.setMessage(review.getMessage());
        holder.setRating(review.getRating());
        holder.setPhoto(review.getPhoto());
        switch (holder.getType()) {
            case REVIEW_PLACE_TYPE:
                    ((ReviewPlaceViewHolder) holder).setName(review.getProfileName());
                    ((ReviewPlaceViewHolder) holder).setProfilePhoto(review.getProfilePhotoUrl());
                break;
            case REVIEW_PROFILE_TYPE:
                ((ReviewProfileViewHolder) holder).setPlaceName(review.getPlaceName());
                ((ReviewProfileViewHolder) holder).setOnReviewPlaceClickListener(
                        profileListener, review.getPlaceId(), review);
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

    public void updateData(List<ReviewModel> reviews) {
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
        @BindView(R.id.photo)
        ImageView photo;
        int type;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setDate(String date) {
            if (date != null) {
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
        }

        public void setMessage(String message) {
            if (message != null && message.length() > 0) {
                profileMessage.setText(message);
            } else {
                profileMessage.setVisibility(View.GONE);
            }
        }

        public void setRating(double rating) {
            ratingBar.setRating((float) rating);
        }

        public int getType() {
            return this.type;
        }

        public void setPhoto(String photoUrl) {
            if (photoUrl != null && photoUrl.length() > 0) {
                Glide.with(itemView.getContext())
                        .load(API_UPLOAD_URL + photoUrl)
                        .centerCrop()
                        .crossFade()
                        .into(photo);

            } else {
                photo.setVisibility(View.GONE);
            }
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

        public void setProfilePhoto(String photoUrl) {
            if (photoUrl != null) {
                Glide.with(itemView.getContext())
                        .load(Constants.API_UPLOAD_URL + photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .centerCrop()
                        .crossFade()
                        .placeholder(R.mipmap.profile)
                        .into(profilePhoto);
            }
        }

    }

    public class ReviewProfileViewHolder extends ReviewViewHolder implements PopupMenu.OnMenuItemClickListener {
        @BindView(R.id.review_place_name)
        TextView placeName;
        @BindView(R.id.options_btn)
        ImageButton optionsBtn;

        public ReviewProfileViewHolder(View itemView) {
            super(itemView);
            this.type = REVIEW_PROFILE_TYPE;
        }

        public void setPlaceName(String placeName) {
            this.placeName.setText(placeName);
        }

        public void setOnReviewPlaceClickListener(final ReviewProfileListener listener,
                                                  final String placeId,
                                                  final ReviewModel review) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onReviewProfileClick(placeId);
                }
            });
            optionsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu(view);
                }
            });
        }

        private void popupMenu(View view) {
            PopupMenu menu = new PopupMenu(this.itemView.getContext(), view);
            menu.setOnMenuItemClickListener(this);
            menu.inflate(R.menu.my_profile_review);
            menu.setGravity(0);
            menu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            ReviewAdapter.this.handleReviewItem(item, getAdapterPosition());
            return false;
        }
    }

    private void handleReviewItem(MenuItem item, int adapterPosition) {
        if (reviews != null && adapterPosition < reviews.size()) {
            ReviewModel review = reviews.get(adapterPosition);
            reviewMenuListener.onReviewMenuItemClick(item, review);
        }

    }

    public interface ReviewProfileListener {
        void onReviewProfileClick(String placeId);
    }

    public interface ReviewMenuListener {
        void onReviewMenuItemClick(MenuItem item, ReviewModel review);
    }
}
