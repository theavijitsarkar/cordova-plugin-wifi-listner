package cordova.plugin.wifi.listner;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.IntentFilter;
import android.net.wifi.*;


/**
 * This class echoes a string called from JavaScript.
 */
public class wifilistner extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        String mac = args.getString(0);
        String text = args.getString(2);
        String title = args.getString(1);
        BroadcastReceiver broadcastReceiver = new WifiChange(mac, getApplicationContext(),title,text);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        if (action.equals("startTracker")) {
            getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
            return true;
        }

        if (action.equals("stopTracker")) {
            getApplicationContext().registerReceiver(broadcastReceiver);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
