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


import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/**
 * This class echoes a string called from JavaScript.
 */
public class wifilistner extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        String mac = args.getString(0);
        
        String title = args.getString(1);
        String nowconnected = args.getString(2);
        String nowdisconnected = args.getString(3);





        final Context context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("startTracker")) {
            
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                Editor edit = settings.edit();
                edit.putString("mac", "00:17:7C:6B:C1:83");
                edit.putString("nowconnected", nowconnected);
                edit.putString("nowdisconnected", nowdisconnected);
                edit.putBoolean("active", true);
                edit.apply();
                return true;
        }

        if (action.equals("stopTracker")) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                Editor edit = settings.edit();
                edit.putBoolean("active",false);
                edit.apply();
            return true;
        }
        return false;
    }

}
