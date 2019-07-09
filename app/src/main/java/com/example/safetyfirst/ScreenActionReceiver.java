package com.example.safetyfirst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ScreenActionReceiver extends BroadcastReceiver {

    private String TAG = "ScreenActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*

        String action = intent.getAction();


        if(Intent.ACTION_SCREEN_ON.equals(action))
        {
            Log.d(TAG, "screen is on...");

        }

        else if(Intent.ACTION_SCREEN_OFF.equals(action))
        {
            Log.d(TAG, "screen is off...");
        }

        else if(Intent.ACTION_USER_PRESENT.equals(action))
        {
            Log.d(TAG, "screen is unlock...");
        }
        */
    }


    public IntentFilter getFilter(){
        final IntentFilter filter = new IntentFilter();
        /*
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        */
        return filter;
    }

}