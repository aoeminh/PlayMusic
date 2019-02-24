package com.example.apple.playmusic.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Connection extends BroadcastReceiver {

    public static final String CONNECTION= "connection";
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!isOnline(context)){
            Toast.makeText(context,"No connection internet",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline(Context context){
        boolean isConnect= false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo[] networkInfo =  connectivityManager.getAllNetworkInfo();
        for (NetworkInfo n: networkInfo
             ) {
            if(n.getState() == NetworkInfo.State.CONNECTED){
                isConnect=true;
            }
        }
        return isConnect;

    }
}
