package com.blancgrupo.apps.tripguide.presentation.ui.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;

import com.blancgrupo.apps.tripguide.R;

/**
 * Created by root on 9/4/17.
 */

public class ExploreMyCityView extends FrameLayout {
    Button btn;

    public ExploreMyCityView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.explore_nearby_layout, this);
        btn = findViewById(R.id.btn);
    }

    public ExploreMyCityView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.explore_nearby_layout, this);
        btn = findViewById(R.id.btn);
    }
}
