package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Photo;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.DisplayImageFragment;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.robohorse.pagerbullet.PagerBullet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DisplayImageActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    PagerBullet viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        ButterKnife.bind(this);
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
            List<Photo> photos = data.getParcelableArrayList(Constants.EXTRA_IMAGE_URL);
            viewPager.setAdapter(new PhotosPageAdapter(getSupportFragmentManager(), photos));
            viewPager.setIndicatorTintColorScheme(Color.WHITE, Color.DKGRAY);

        } else {
            Toast.makeText(this, R.string.no_image_found, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class PhotosPageAdapter extends FragmentPagerAdapter {

        List<Photo> photos;

        public PhotosPageAdapter(FragmentManager fm, List<Photo> photos) {
            super(fm);
            this.photos = photos;
        }

        @Override
        public Fragment getItem(int position) {
            Photo photo = photos.get(position);
            DisplayImageFragment fragment = new DisplayImageFragment();
            Bundle args = new Bundle();
            args.putString(Constants.EXTRA_IMAGE_URL, ApiUtils.getPlacePhotoUrl(
                    (MyApplication) getApplication(), photo.getReference(), photo.getWidth()
            ));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            if (photos != null) {
                return photos.size();
            }
            return 0;
        }
    }
}
