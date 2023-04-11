package com.prox.sunrisetodolistapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.prox.sunrisetodolistapp.Home;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.Utility.NotificationHelperClass;

public class AlertReceiver extends BroadcastReceiver {

    public static MutableLiveData<Boolean> finished = new MutableLiveData<Boolean>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int checker = intent.getIntExtra("checker",-1);
        int id = intent.getIntExtra("id",-1);

        switch (checker){
            case 1:
                break;
            case 2:
                NotificationHelperClass.sendTaskNotification(context,"Get Started",title);
                break;
            case 3:
                Home.TASKID = id;
                Home.TaskName = title;
                NotificationHelperClass.sendTaskNotification(context,"Due Task!",title);
                for (TaskModel model: Prevalent.todaySTasks){
                    if (model.getId() == id){
                        Prevalent.todaySTasks.remove(model);
                        Prevalent.overDueTasks.add(model);
                    }
                }
                finished.setValue(true);
                break;
                case 4:
                NotificationHelperClass.sendHabitNotification(context,"Maintain Habit",title);
                break;
        }

    }
}
