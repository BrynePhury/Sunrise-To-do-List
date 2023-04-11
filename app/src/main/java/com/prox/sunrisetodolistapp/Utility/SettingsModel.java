package com.prox.sunrisetodolistapp.Utility;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SettingsModel extends RealmObject {
    @PrimaryKey
    int id = 1;

    String noteFontSize;
    boolean disappearingTasksEnabled;
    boolean alarmAlertsEnabled;
    boolean hideHabitGoals;

    public boolean isHideHabitGoals() {
        return hideHabitGoals;
    }

    public void setHideHabitGoals(boolean hideHabitGoals) {
        this.hideHabitGoals = hideHabitGoals;
    }

    public String getNoteFontSize() {
        return noteFontSize;
    }

    public void setNoteFontSize(String noteFontSize) {
        this.noteFontSize = noteFontSize;
    }

    public boolean isDisappearingTasksEnabled() {
        return disappearingTasksEnabled;
    }

    public void setDisappearingTasksEnabled(boolean disappearingTasksEnabled) {
        this.disappearingTasksEnabled = disappearingTasksEnabled;
    }

    public boolean isAlarmAlertsEnabled() {
        return alarmAlertsEnabled;
    }

    public void setAlarmAlertsEnabled(boolean alarmAlertsEnabled) {
        this.alarmAlertsEnabled = alarmAlertsEnabled;
    }
}
