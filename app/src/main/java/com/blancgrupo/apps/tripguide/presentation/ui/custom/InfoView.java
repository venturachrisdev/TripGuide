package com.blancgrupo.apps.tripguide.presentation.ui.custom;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.utils.TextStringUtils;

/**
 * Created by root on 9/4/17.
 */

public class InfoView extends FrameLayout {
    TextView titleText;
    TextView bodyText;
    ImageView expandView;
    ImageView audioView;
    boolean expanded = false;
    String content;


    public InfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.place_info_layout, this);
        titleText = findViewById(R.id.textView1);
        bodyText = findViewById(R.id.textView2);
        audioView = findViewById(R.id.imageAudio);
        expandView = findViewById(R.id.imageView3);
        expandView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleText();
            }
        });
        audioView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InfoView.this.getContext(),
                        R.string.coming_soon,
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (content != null) {
                    ClipboardManager clipboardManager = (ClipboardManager)
                            InfoView.this.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Info", content);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(InfoView.this.getContext(),
                            R.string.copied_to_clipboard,
                            Toast.LENGTH_LONG)
                            .show();
                }
                return false;
            }
        });
    }

    public void setContentText(String text) {
        this.content = text;
        toggleText();
    }

    public void toggleText() {
        if (this.content != null) {
            if (expanded) {
                expandView.setBackground(
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_less_white_24dp)
                );
                audioView.setVisibility(VISIBLE);
                this.bodyText.setText(this.content);


            } else {
                expandView.setBackground(
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_more_white_24dp)
                );
                this.bodyText.setText(TextStringUtils.shortText(this.content));
                audioView.setVisibility(GONE);
            }
            expanded = !expanded;
        }
    }
}
