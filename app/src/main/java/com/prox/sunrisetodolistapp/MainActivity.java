package com.prox.sunrisetodolistapp;

import static com.prox.sunrisetodolistapp.Utility.HelperClass.refreshTasks;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.Services.MyJobService;
import com.prox.sunrisetodolistapp.Utility.SettingsModel;

import io.realm.Realm;
import io.realm.RealmQuery;

public class MainActivity extends AppCompatActivity {

    Realm realm;

    public static final int JOB_ID = 900;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        realm = Realm.getDefaultInstance();

        RealmQuery<SettingsModel> settingsModelRealmQuery = realm.where(SettingsModel.class);

        SettingsModel settingsModel = settingsModelRealmQuery.findFirst();

        if (settingsModel != null) {
            Prevalent.settingsModel = settingsModel;
        } else {
            Prevalent.settingsModel = new SettingsModel();
            Prevalent.settingsModel.setDisappearingTasksEnabled(false);
            Prevalent.settingsModel.setNoteFontSize("Small");
            Prevalent.settingsModel.setHideHabitGoals(true);
        }

        refreshTasks(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Start home activity
                startActivity(new Intent(MainActivity.this, Home.class));
                finish();

            }
        }, 500);

        scheduleJob();

    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setPersisted(true)
                .setPeriodic(1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
    }


}