package com.blancgrupo.apps.tripguide.presentation.ui.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;

/**
 * Created by root on 9/4/17.
 */

public class FeatureCardView extends FrameLayout {
    ImageView closeBtn;
    TextView titleText;
    TextView bodyText;
    Button goBtn;

    public FeatureCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.card_explore_tour_layout, this);
        closeBtn = findViewById(R.id.imageView3);
        titleText = findViewById(R.id.textView1);
        bodyText = findViewById(R.id.textView2);
        goBtn = findViewById(R.id.btn);

        closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }
    public void hide() {
        this.setVisibility(GONE);
    }
}
