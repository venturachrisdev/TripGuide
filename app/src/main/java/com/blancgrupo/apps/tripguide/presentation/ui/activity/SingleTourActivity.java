package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.data.entity.api.Tour;
import com.blancgrupo.apps.tripguide.data.entity.api.TourWrapper;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.TourViewModel;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleTourActivity extends AppCompatActivity
    implements LifecycleRegistryOwner {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @Inject
    TourVMFactory tourVMFactory;
    TourViewModel tourViewModel;
    private LifecycleRegistry registry = new LifecycleRegistry(this);
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tour);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        tourViewModel = ViewModelProviders.of(this, tourVMFactory)
                .get(TourViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data != null && data.containsKey(Constants.EXTRA_SINGLE_TOUR_ID)) {
            imageUrl = data.getString(Constants.EXTRA_IMAGE_URL);
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(headerImage);
            String id = data.getString(Constants.EXTRA_SINGLE_TOUR_ID);
            tourViewModel.getSingleTour(id).observe(this, new Observer<TourWrapper>() {
                @Override
                public void onChanged(@Nullable TourWrapper tourWrapper) {
                    if (tourWrapper != null) {
                        Tour tour = tourWrapper.getTour();
                        if (tour != null) {
                            bindTour(tour);
                        } else {
                            //TODO: Handle
                            Toast.makeText(SingleTourActivity.this,
                                    R.string.network_error, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // TODO: Handle
                        Toast.makeText(SingleTourActivity.this,
                                R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindTour(Tour tour) {
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.registry;
    }
}
