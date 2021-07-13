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
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    @Override
    public void onReceive(Context context, Intent intent) {
//
//        Intent i = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, i, 0);

//        Bundle bundle = intent.getExtras();
//        String name = (String) bundle.get("name");
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.toolbar_logo)
//                .setContentTitle("Forget-Me-Not")
//                .setContentText( name + " is going to expire!")
//
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//                //.setContentIntent(pendingIntent)
//                //.setCategory(NotificationCompat.CATEGORY_REMINDER)
//                //.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
//                //.setOnlyAlertOnce(true);
//
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        int notificationId = 123;
//        notificationManagerCompat.notify(notificationId, builder.build());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;
    }

}