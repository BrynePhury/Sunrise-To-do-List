package com.prox.sunrisetodolistapp.Prevalent;

import com.prox.sunrisetodolistapp.Models.BudgetItem;
import com.prox.sunrisetodolistapp.Models.BudgetModel;
import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Models.NoteModel;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Utility.SettingsModel;

import java.util.ArrayList;

public class Prevalent {

    //This is a class where I store stuff i will need regularly throughout the app

    //TASKS
    public static ArrayList<TaskModel> allTasks;

    public static ArrayList<TaskModel> todaySTasks;
    public static ArrayList<TaskModel> tomorrowsTasks;
    public static ArrayList<TaskModel> laterDatesTasks;
    public static ArrayList<TaskModel> overDueTasks;
    public static ArrayList<TaskModel> pastTasks;

    //GOALS
    public static ArrayList<GoalModel> goalModels;

    //NOTES
    public static ArrayList<NoteModel> allNotes;
    public static ArrayList<NoteModel> pinnedNotes;
    public static ArrayList<NoteModel> archivedNotes;

    //BUDGETS
    public static ArrayList<BudgetModel> allBudgets;
    public static ArrayList<BudgetItem> allBudgetItems = new ArrayList<>();
    public static ArrayList<BudgetModel> archivedBudgets;


    public static SettingsModel settingsModel;




}
