/*
MyHaruna - Display a Yuru-chara
Copyright (C) 2021  iamsqy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package com.iamsqy.myharuna;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int CODE_WINDOW = 0;
    Button button;
    Button buttonOpen;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private BroadcastReceiver batteryLevelRcvr;
    private IntentFilter batteryLevelFilter;

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            Log.e("serviceName：", serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().contains(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        buttonOpen = (Button) findViewById(R.id.buttonOpen);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        monitorBatteryState();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, R.string.need_flow_permission, Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), CODE_WINDOW);
            }
        }
        String CHANNEL_ID = "exit";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.quit), NotificationManager.IMPORTANCE_MIN);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Intent nintent = new Intent(MainActivity.this, Quit.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, nintent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.Haruna_is_running))
                .setContentText(getString(R.string.Haruna_hello))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.click_to_quit)))
                .setPriority(Notification.PRIORITY_LOW)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
        //通过 builder.build() 拿到 notification
        mNotificationManager.notify(1, mBuilder.build());
    }

    public void button_click(View view) {
        Intent newIntent = new Intent(this, TestService.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.stopService(newIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, R.string.need_flow_permission, Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), CODE_WINDOW);
            }
        }
        String CHANNEL_ID = "exit";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "退出", NotificationManager.IMPORTANCE_MIN);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mNotificationManager.cancel(1);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    public void buttonOpen_click(View view) {
        Intent intent = new Intent(this, TestService.class);
        boolean isRunning = isServiceRunning(this, "TestService");
        stopService(intent);
        if (!isRunning) {
            startService(intent);
        }
    }
    private void monitorBatteryState() {
        batteryLevelRcvr = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int status = intent.getIntExtra("status", -1);
                int health = intent.getIntExtra("health", -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                if (level <= 30) {
                    Toast.makeText(context, R.string.Haruna_low_batt, Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton(R.string.Haruna_hide, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    builder.setMessage(R.string.Haruna_batt_confirm)
                            .setTitle(R.string.Haruna_low_batt_found);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        };
        batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelRcvr, batteryLevelFilter);
    }
}
