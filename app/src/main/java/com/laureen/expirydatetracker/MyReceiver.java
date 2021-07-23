package com.laureen.expirydatetracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "1000";
    public static int NOTIFICATION_ID = 1;
    //public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);

        Bundle bundle = intent.getExtras();
        String name = (String) bundle.get("name");
        String days = (String) bundle.get("days");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.toolbar_logo)
                .setContentTitle("Reminder: Expiry Date Tracker")
                .setContentText(name + " is expiring in " + days + " days!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true);
        managerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}