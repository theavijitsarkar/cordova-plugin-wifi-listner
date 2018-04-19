package com.avifa.wifilistner;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;



import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.IntentFilter;
import android.net.wifi.*;


import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * This class echoes a string called from JavaScript.
 */
public class wifilistner extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        String mac = args.getString(0);
        String text = args.getString(2);
        String title = args.getString(1);
        
        final Context context = this.cordova.getActivity().getApplicationContext();
        
        BroadcastReceiver broadcastReceiver = new WifiChange(mac, context ,title,text);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        

        if (action.equals("startTracker")) {
            webView.getContext().registerReceiver(broadcastReceiver, intentFilter);
            return true;
        }

        if (action.equals("stopTracker")) {
            webView.getContext().unregisterReceiver(broadcastReceiver);
            return true;
        }
        return false;
    }

}
