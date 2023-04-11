package com.prox.sunrisetodolistapp.Utility;

import static com.prox.sunrisetodolistapp.MyApplication.HABIT_CHANNEL;
import static com.prox.sunrisetodolistapp.MyApplication.TASK_CHANNEL;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.prox.sunrisetodolistapp.R;

public class NotificationHelperClass {

    public static void sendTaskNotification(Context context, String title, String description){

        Notification taskNotification = new NotificationCompat.Builder(context, TASK_CHANNEL)
                .setSmallIcon(R.drawable.ic_sunrise)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(101,taskNotification);

    }
    public static void sendHabitNotification(Context context, String title, String description){

        Notification habitNotification = new NotificationCompat.Builder(context, HABIT_CHANNEL)
                .setSmallIcon(R.drawable.ic_sunrise)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(107,habitNotification);

    }
}
