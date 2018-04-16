package cordova.plugin.wifi.listner;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.*;
import 	android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.*;


public class WifiChange extends BroadcastReceiver  {
    public String mac;
    public String title;
    public String text;
    public Context contextMain;

    public WifiChange(String mac,Context context,String title,String text){
            this.mac = mac;
        this.contextMain = context;
        this.title = title;
        this.text= text;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION .equals(action)) {
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (SupplicantState.isValidState(state)
                    && state == SupplicantState.COMPLETED) {

                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                WifiInfo wifi = wifiManager.getConnectionInfo();
                if (wifi != null) {
                    // get current router Mac address
                    String bssid = wifi.getBSSID();

                    boolean connected = this.mac.toUpperCase().equals(bssid.toUpperCase());
//                    Toast.makeText(context.getApplicationContext(),"BSSID: "+bssid+"Connected : "+connected,Toast.LENGTH_SHORT).show();
                            if(connected){

                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(context.getApplicationContext())
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle(this.title)
                                                .setContentText(this.text);

                                Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                                PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(contentIntent);

                                // Add as notification
                                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(0, builder.build());



                            }

                }



            }
        }
    }


}
