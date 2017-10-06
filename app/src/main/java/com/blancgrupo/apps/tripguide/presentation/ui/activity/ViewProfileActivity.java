package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ProfileDBRepository;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ReviewDBRepository;
import com.blancgrupo.apps.tripguide.domain.model.ProfileModel;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileViewModel;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewProfileActivity extends AppCompatActivity implements ReviewAdapter.ReviewProfileListener {

    String profileId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_rv)
    ShimmerRecyclerView recyclerView;
    @BindView(R.id.user_profile_name)
    TextView profileName;
    @BindView(R.id.user_profile_email)
    TextView profileEmail;
    @BindView(R.id.user_profile_image)
    CircleImageView profileImage;
    @BindView(R.id.review_number)
    TextView reviewNumber;
    @BindView(R.id.review_number2)
    TextView reviewNumber2;
    @BindView(R.id.xp_progress)
    MaterialProgressBar XP_Progress;
    @BindView(R.id.review_title)
    TextView reviewTitleText;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;

    @Inject
    ProfileVMFactory profileVMFactory;
    @Inject
    ProfileRepository profileRepository;
    @Inject
    ProfileDBRepository profileDBRepository;
    @Inject
    ReviewDBRepository reviewDBRepository;

    ProfileViewModel profileViewModel;
    ReviewAdapter reviewAdapter;
    ActionBar actionBar;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.profile);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.EXTRA_VIEW_PROFILE_ID)) {
            profileId = extras.getString(Constants.EXTRA_VIEW_PROFILE_ID);
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            finish();
        }

        profileViewModel = ViewModelProviders.of(this, profileVMFactory)
                .get(ProfileViewModel.class);
        reviewAdapter = new ReviewAdapter(ReviewAdapter.REVIEW_PROFILE_TYPE, this, null);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_profile_layout,
                recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(
                reviewAdapter,
                null,
                emptyView,
                null
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectivityUtils.isConnected(ViewProfileActivity.this)){
                    refreshProfile();
                } else {
                    Toast.makeText(ViewProfileActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void initializeProfileLayout(ProfileWithReviews profileWithReviews) {
        ProfileModel profile = profileWithReviews.getProfile();
        List<ReviewModel> reviews = profileWithReviews.getReviews();
        swipeRefreshLayout.setRefreshing(false);
        if (profile != null) {
            swipeRefreshLayout.setRefreshing(false);
            bindProfile(profile);

            if (reviews !=  null && reviews.size() > 0) {
                reviewAdapter.updateData(null);
                reviewAdapter.updateData(reviews);
                statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
            } else {
                statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
            }
        }
    }

    private void refreshProfile() {
        if (ConnectivityUtils.isConnected(this)) {
            if (!profileViewModel.loadSingleProfile(profileId)) {
                profileViewModel.getSingleProfile(profileId).observe(this, new Observer<ProfileWithReviews>() {
                    @Override
                    public void onChanged(@Nullable ProfileWithReviews profile) {
                        initializeProfileLayout(profile);
                    }
                });
            }
        } else {
            profileDBRepository.getProfile(profileId).observe(this, new Observer<ProfileWithReviews>() {
                @Override
                public void onChanged(@Nullable ProfileWithReviews profile) {
                    initializeProfileLayout(profile);
                }
            });
        }
    }

    public int calculateXP_progress(int experience) {
        float progress;
        reviewNumber.setText(String.format(getString(R.string.xp_count), experience));
        if (experience < Constants.XP_FIRST_LEVEL_TOP) {
            float rat = (Float.valueOf(experience) / Float.valueOf(Constants.XP_FIRST_LEVEL_TOP));
            reviewNumber2.setText(String.format("/ " + getString(R.string.xp_count), Constants.XP_FIRST_LEVEL_TOP));
            reviewTitleText.setText(R.string.visitor);
            progress = 100 * rat;
        } else if (experience < Constants.XP_SECOND_LEVEL_TOP) {
            reviewNumber2.setText(String.format("/ " + getString(R.string.xp_count), Constants.XP_SECOND_LEVEL_TOP));
            reviewTitleText.setText(R.string.traveler);
            progress = 100 * (Float.valueOf(experience)/ Float.valueOf(Constants.XP_SECOND_LEVEL_TOP));
        } else {
            reviewNumber2.setText(String.format("/ " + getString(R.string.xp_count), Constants.XP_THIRD_LEVEL_TOP));
            reviewTitleText.setText(R.string.scout);
            progress = 100 * (Float.valueOf(experience) / Float.valueOf(Constants.XP_THIRD_LEVEL_TOP));
        }
        return (int) progress;
    }

    private void bindProfile(ProfileModel profile) {
        actionBar.setTitle(profile.getName());
        toolbar.setTitle(profile.getName());
        profileName.setText(profile.getName());
        profileEmail.setText(profile.getEmail());
        if (profile.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(Constants.API_UPLOAD_URL + profile.getPhotoUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.mipmap.profile)
                    .into(profileImage);
        }
        int progress = calculateXP_progress(profile.getExperience());
        XP_Progress.setProgress(progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshProfile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReviewProfileClick(String placeId) {
        Intent i = new Intent(this, PlaceDetailActivity.class);
        i.putExtra(Constants.EXTRA_PLACE_ID, placeId);
        startActivity(i);
        finish();
    }

    @Override
    public void onReviewUserClick(String userId) {

    }
}
