package com.prox.sunrisetodolistapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;

public class MyApplication extends Application {
    public static final String TASK_CHANNEL = "TaskChannel";
    public static final String HABIT_CHANNEL = "HabitChannel";
    public static final String MORNING_REMINDER_ID = "MorningReminderChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        Realm.init(this);
        
        createNotificationChannels();
        
    }

    private void createNotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel taskChannel = new NotificationChannel(
                    TASK_CHANNEL,
                    "Tasks Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            taskChannel.enableVibration(true);
            taskChannel.setDescription("This is the channel that Task notifications come through");

            NotificationChannel morningReminderChannel = new NotificationChannel(
                    MORNING_REMINDER_ID,
                    "morning reminder Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            morningReminderChannel.enableVibration(true);
            morningReminderChannel.setDescription("This is the channel that the morning reminder come through");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(taskChannel);
            manager.createNotificationChannel(morningReminderChannel);
        }

    }
}
