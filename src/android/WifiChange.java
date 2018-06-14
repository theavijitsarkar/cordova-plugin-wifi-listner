package com.avifa.wifilistner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.NotificationManager;
import android.content.*;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.Service;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.SupplicantState;

public class WifiChange extends BroadcastReceiver  {
    public String mac;
    public String title;
    public String text;
    public Context contextMain;
 
    public WifiChange(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Log.d("WIFI", "Received Intent "+intent.getAction().toString());

        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

        SupplicantState supState;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        supState = wifiInfo.getSupplicantState();

        Log.d("WIFI", "Current State : "+ supState.name().toString());


        if( supState.equals(SupplicantState.DISCONNECTED) ||supState.equals( SupplicantState.INACTIVE )||supState.equals( SupplicantState.UNINITIALIZED) ){
            //Set last ssid as blank
            Log.d("WIFI", "Setting last know as blank because wifi is disconnected");
            SharedPreferences.Editor edit = settings.edit();
            edit.putString("lastSsid", "");
            edit.apply();

            if (settings.getBoolean("active", false)) {
                context.getApplicationContext().startService(new Intent(context.getApplicationContext(), WifiActiveService.class));
            }
        }

        if ( supState.equals(SupplicantState.COMPLETED) ) {
            Log.d("WIFI", "Starting Service");
            if (settings.getBoolean("active", false)) {
                context.getApplicationContext().startService(new Intent(context.getApplicationContext(), WifiActiveService.class));
            }
        }

    }

    public static class WifiActiveService extends Service {


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            Log.d("WIFI", "Service Started");
            final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            final String imac = settings.getString("mac", "");
            final String lastSsid = settings.getString("lastSsid", "");
            final String title = settings.getString("title", "");
            final String nowconnected = settings.getString("nowconnected", "");
            final String nowdisconnected = settings.getString("nowdisconnected", "");
 


            // Need to wait a bit for the SSID to get picked up;
            // if done immediately all we'll get is null
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("WIFI", "RUNNING RUNNABLE");
                    Boolean run = true;
                    while(run){


                        WifiInfo info = wifiManager.getConnectionInfo();
                        String ssid = info.getBSSID();
                        Log.d("WIFI", "The SSID & LAST SSID are " + ssid +" "+lastSsid );

                        if(ssid == null){
                            run = true;
                            try {
                                Log.d("WIFI", "PAUSING FOR 5000");
                                Thread.sleep(5000);
                                Log.d("WIFI", "RESMUE");
                            }catch(Exception e){
                                Log.d("WIFI", "EXCEPTION FROM THREAD PAUSE");
                            }

                        }else{

                            Log.d("WIFI", "Last Known Wifi : "+lastSsid+" New Wifi  : "+ssid);

                            //it was connected, but not anymore
                            if(lastSsid.toUpperCase().equals(imac.toUpperCase()) && !ssid.toUpperCase().equals(imac))
                            {
                                createNotification(title, nowdisconnected);

                            }
                            else if(!lastSsid.toUpperCase().equals(imac.toUpperCase()) && ssid.toUpperCase().equals(imac))
                            {//it was not connected but now connected

                                createNotification(title, nowconnected);
                            }

                            run = false;

                            //Setting lastknown wifi
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = settings.edit();
                            edit.putString("lastSsid", ssid);
                            edit.apply();

                            stopSelf();

                        }
                    }

                }
            }, 10000);
            return START_NOT_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        /**
         * Creates a notification displaying the SSID & MAC addr
         */
        private void createNotification(String title, String text) {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification n = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSound(soundUri)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text))
                    //.setSmallIcon(R.mipmap.ic_launcher)
                    .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
                    .build();
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .notify(0, n);
        }
    }
}
