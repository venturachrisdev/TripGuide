package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DisplayImageActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.image)
    PhotoView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle data =  intent.getExtras();
        if (data != null && data.containsKey(Constants.EXTRA_IMAGE_URL)) {
            Glide.with(this)
                    .load(data.getString(Constants.EXTRA_IMAGE_URL))
                    .fitCenter()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setIndeterminate(false);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(DisplayImageActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                    DisplayImageActivity.this.finish();
                                }
                            }, 300);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setIndeterminate(false);
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
