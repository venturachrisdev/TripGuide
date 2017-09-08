package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.ParentTour;
import com.blancgrupo.apps.tripguide.data.entity.api.ParentTourWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TourActivity extends AppCompatActivity
        implements LifecycleRegistryOwner {

    @Inject
    TourVMFactory tourVMFactory;
    TourViewModel tourViewModel;
    private LifecycleRegistry registry = new LifecycleRegistry(this);
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView toolbarTitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbarTitle.setText(R.string.app_name);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        int transparent = ContextCompat
                .getColor(getApplicationContext(),android.R.color.transparent);
        toolbar.setTitleTextColor(transparent);
        toolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(transparent));
        toolbarLayout.setCollapsedTitleTextColor(ColorStateList.valueOf(transparent));
        DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);
        tourViewModel = ViewModelProviders.of(this, tourVMFactory)
                .get(TourViewModel.class);
        Intent i = getIntent();
        Bundle data = i.getExtras();
        if (data == null || !data.containsKey(Constants.EXTRA_PLACE_TOUR_ID)) {
            Toast.makeText(this, R.string.no_tour_selected, Toast.LENGTH_LONG).show();
            finish();
        }
        String id = data.getString(Constants.EXTRA_PLACE_TOUR_ID);
        tourViewModel.getParentTour(id).observe(this, new Observer<ParentTourWrapper>() {
            @Override
            public void onChanged(@Nullable ParentTourWrapper parentTourWrapper) {
                if (parentTourWrapper != null) {
                    ParentTour parentTour = parentTourWrapper.getPlace();
                    if (parentTour != null && parentTourWrapper.getStatus().equals("OK")) {
                        bindParentTour(parentTour);
                        //statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                    } else {
                        Toast.makeText(TourActivity.this, parentTourWrapper.getStatus(),
                                Toast.LENGTH_SHORT).show();
                        //statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                    }
                } else {
                    Toast.makeText(TourActivity.this, "Dude is null", Toast.LENGTH_SHORT).show();
                    //statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
                }
            }
        });
    }

    private void bindParentTour(ParentTour parentTour) {
        toolbarTitle.setText(parentTour.getName());
        Photo photo = parentTour.getPhoto();
        if (photo != null) {
            Glide.with(this)
                    .load(ApiUtils.getPlacePhotoUrl((MyApplication) getApplication(), photo.getReference(),
                            photo.getWidth()))
                    .centerCrop()
                    .crossFade()
                    .into(headerImage);
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }
}
