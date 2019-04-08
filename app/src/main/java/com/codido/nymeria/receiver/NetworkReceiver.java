package com.codido.nymeria.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.NetworkUtils;

/**
 * 网络连接的receiver
 */
public class NetworkReceiver extends BroadcastReceiver {

    //private ServiceConnection conn;
    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Global.debug("bpascal NetworkReceiver onReceive");
        boolean networkConnected = NetworkUtils.isNetworkConnected(context);
        if (networkConnected) {

        } else {
            Toast.makeText(context, "没有可以连接的网络", Toast.LENGTH_SHORT).show();
        }
    }
}
