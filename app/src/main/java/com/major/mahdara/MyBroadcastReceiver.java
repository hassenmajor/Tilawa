package com.major.mahdara;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            MainActivity.mediaPlayer.pause();
        } catch (Exception e) { }
    }
}
