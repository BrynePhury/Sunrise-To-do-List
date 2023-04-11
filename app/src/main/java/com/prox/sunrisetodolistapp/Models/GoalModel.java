package com.prox.sunrisetodolistapp.Models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GoalModel extends RealmObject {

    public static int TYPE_HABIT = 1;
    public static int TYPE_CHECKLIST = 2;

    @PrimaryKey
    int ID;

    String goal_name;
    int completion_percentage;
    int pointsCount;
    int totalPoint;
    String note;
    int type;
    Date completion_date;
    Date daily_time;
    boolean sunday;
    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;

    public GoalModel() {
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public Date getDaily_time() {
        return daily_time;
    }

    public void setDaily_time(Date daily_time) {
        this.daily_time = daily_time;
    }

    public static int getTypeHabit() {
        return TYPE_HABIT;
    }

    public static void setTypeHabit(int typeHabit) {
        TYPE_HABIT = typeHabit;
    }

    public static int getTypeChecklist() {
        return TYPE_CHECKLIST;
    }

    public static void setTypeChecklist(int typeChecklist) {
        TYPE_CHECKLIST = typeChecklist;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGoal_name() {
        return goal_name;
    }

    public void setGoal_name(String goal_name) {
        this.goal_name = goal_name;
    }

    public int getCompletion_percentage() {
        return completion_percentage;
    }

    public void setCompletion_percentage(int completion_percentage) {
        this.completion_percentage = completion_percentage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(Date completion_date) {
        this.completion_date = completion_date;
    }
}
