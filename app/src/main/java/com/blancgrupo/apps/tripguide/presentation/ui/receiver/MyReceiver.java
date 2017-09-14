package com.blancgrupo.apps.tripguide.presentation.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.utils.Constants;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra(Constants.EXTRA_PROGRESS), Toast.LENGTH_SHORT).show();
    }
}
