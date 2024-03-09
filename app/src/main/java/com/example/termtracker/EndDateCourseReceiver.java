package com.example.termtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class EndDateCourseReceiver extends BroadcastReceiver {
    private int NOTIFICATION_ID = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String END_CHANNEL_ID = "course_end_ID";

        createNotificationChannel(context, END_CHANNEL_ID);

        Notification EndNotification = new NotificationCompat.Builder(context, END_CHANNEL_ID)
                .setSmallIcon(R.drawable.bell_solid)
                .setContentTitle("COURSE NOTIFICATION!!")
                .setContentText(intent.getStringExtra("COURSE_ENDING"))
                .setAutoCancel(true)
                .build();

        // Get compatibility NotificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Post notification using ID.  If same ID, this notification replaces previous one

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID++, EndNotification);


    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        CharSequence name = "channel_name";
        String description = "channel_description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
