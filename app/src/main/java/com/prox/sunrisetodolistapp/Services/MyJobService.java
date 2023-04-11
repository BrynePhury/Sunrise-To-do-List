package com.prox.sunrisetodolistapp.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Receiver.AlertReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MyJobService extends android.app.job.JobService {
    private boolean jobCanceled;

    @Override
    public boolean onStartJob(JobParameters params) {

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        Realm realm = Realm.getInstance(configuration);

        //Get Habit Goals
        ArrayList<GoalModel> goalModels = new ArrayList<>();

        RealmQuery<GoalModel> goalModelRealmQuery = realm.where(GoalModel.class);

        RealmResults<GoalModel> goalModelRealmResults = goalModelRealmQuery.findAll();

        goalModels.addAll(goalModelRealmResults);

        for (GoalModel goalModel : goalModels) {
            if (jobCanceled){
                break;
            }
            if (goalModel.getType() == GoalModel.TYPE_HABIT) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                switch (day) {
                    case Calendar.SUNDAY:
                        if (goalModel.isSunday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("goalName", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }

                        break;
                    case Calendar.MONDAY:
                        if (goalModel.isMonday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("goalName", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }
                        break;
                    case Calendar.TUESDAY:
                        if (goalModel.isTuesday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("goalName", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        if (goalModel.isWednesday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("goalName", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }
                        break;
                    case Calendar.THURSDAY:
                        if (goalModel.isThursday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("goalName", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }
                        break;
                    case Calendar.FRIDAY:
                        if (goalModel.isFriday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("title", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }
                        break;
                    case Calendar.SATURDAY:
                        if (goalModel.isSaturday()){
                            Calendar c = Calendar.getInstance();
                            c.setTime(goalModel.getDaily_time());

                            calendar.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(this, AlertReceiver.class);
                            intent.putExtra("goalName", goalModel.getGoal_name());
                            intent.putExtra("checker", 2);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, goalModel.getID(), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        }

                }

            }

        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCanceled = true;
        return true;
    }
}
