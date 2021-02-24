package com.example.dailynews.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public abstract class InternetBroadcastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean internet = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);

        if(!internet)
        {
            Log.d("broadcastInternet","internet on");
            onNetworkOn();
        }
        else
        {
            Log.d("broadcastInternet","internet off");
            onNetworkStop();
        }
    }

    public abstract void onNetworkStop();
    public abstract void onNetworkOn();
}
