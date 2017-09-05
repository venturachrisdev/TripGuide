package com.blancgrupo.apps.tripguide.presentation.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.presentation.ui.activity.DisplayImageActivity;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/5/17.
 */

public class DisplayImageFragment extends Fragment {
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.image)
    PhotoView imageView;
    ImageListener listener;


    public interface ImageListener {
        void onImageError();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.display_image_fragment, container, false);
        ButterKnife.bind(this, root);

        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.EXTRA_IMAGE_URL)) {
            String imageUrl = args.getString(Constants.EXTRA_IMAGE_URL);
            Glide.with(this)
                    .load(imageUrl)
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
//                                    Toast.makeText(DisplayImageActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
//                                    DisplayImageActivity.this.finish();
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
        }
        return root;
    }
}
