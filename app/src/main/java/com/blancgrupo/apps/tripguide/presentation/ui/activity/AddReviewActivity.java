package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.ApiProfileRepository;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddReviewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rating_bar)
    SimpleRatingBar ratingBar;
    @BindView(R.id.review_message)
    TextInputEditText reviewMessageEditText;
    @BindView(R.id.btn)
    Button sendReviewBtn;
    Disposable disposable;
    String placeId;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ProfileRepository profileRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        Bundle data = getIntent().getExtras();
        float rating = data.getFloat(Constants.EXTRA_PROGRESS);
        ratingBar.setRating(rating);
        placeId = data.getString(Constants.EXTRA_PLACE_ID);

        sendReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReview();
            }
        });


    }

    private void
    sendReview() {
        String profileId = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
        if (profileId != null) {
            float rating = ratingBar.getRating();
            String message = reviewMessageEditText.getText().toString();
            Review review = new Review();
            review.setRating(rating);
            review.setMessage(message);
            Review.ReviewProfile profile = new Review.ReviewProfile();
            profile.set_id(profileId);
            review.setProfile(profile);
            Review.ReviewPlace place = new Review.ReviewPlace();
            place.set_id(placeId);
            review.setPlace(place);
            // send review
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Sending review...");
            dialog.setIndeterminate(true);
            dialog.show();
            disposable = profileRepository.addReview(review)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ReviewResponseWrapper>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull ReviewResponseWrapper reviewWrapper) throws Exception {
                            Toast.makeText(AddReviewActivity.this, "Thanks for review this place.", Toast.LENGTH_SHORT).show();
                            dialog.hide();
                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Toast.makeText(AddReviewActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    });
        } else {
            Toast.makeText(this, "You are not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        if (reviewMessageEditText.getText().toString().length() > 0) {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .content("Are you sure?")
                    .positiveText("Discard")
                    .negativeText("Dont Discard")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AddReviewActivity.super.onBackPressed();
                        }
                    })
                    .build();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
