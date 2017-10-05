package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, GettingStartedActivity.class);
        Intent original = getIntent();
        if (original.getAction() != null && original.getAction().equals(Intent.ACTION_VIEW)) {
            intent.setAction(original.getAction());
            intent.setData(original.getData());
        }
        startActivity(intent);
        finish();
    }
}
