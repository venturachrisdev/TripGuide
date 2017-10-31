package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.utils.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CongratsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);
        TextView title = findViewById(R.id.textView1);
        TextView message = findViewById(R.id.textView2);
        String tourName = getIntent().getExtras().getString(Constants.EXTRA_SINGLE_TOUR_ID);
        message.setText(String.format(getString(R.string.you_completed_tour),
                tourName));
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
