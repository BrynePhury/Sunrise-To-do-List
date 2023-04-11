package com.prox.sunrisetodolistapp.Utility;

import android.content.Context;

import com.prox.sunrisetodolistapp.Models.BudgetItem;
import com.prox.sunrisetodolistapp.Models.BudgetModel;
import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Models.NoteModel;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class HelperClass {


    public static void refreshTasks(Context context) {
        Realm.init(context);

        //Create realm instance
        Realm realm = Realm.getDefaultInstance();

        //Create Calender to store dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //store todays date
        Date today = calendar.getTime();

        //store tomorrows date
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        //Query realm
        RealmQuery<TaskModel> taskModelQuery = realm.where(TaskModel.class);

        //Query conditions

        //execute query
        RealmResults<TaskModel> results = taskModelQuery.findAll();

        ArrayList<TaskModel> allTasks = new ArrayList<>();
        ArrayList<TaskModel> todaysTasks = new ArrayList<>();
        ArrayList<TaskModel> tomorrowsTasks = new ArrayList<>();
        ArrayList<TaskModel> laterTasks = new ArrayList<>();
        ArrayList<TaskModel> overDueTasks = new ArrayList<>();
        ArrayList<TaskModel> pastTasks = new ArrayList<>();

        //Iterate through the results
        for (TaskModel model : results) {
            Calendar c = Calendar.getInstance();

            if (!model.isDraft()) {
                //Place eac saved task i its rightful list
                SimpleDateFormat format = new SimpleDateFormat("dd,MM,yyyy", Locale.UK);
                String tomorrowString = format.format(tomorrow);
                String todayString = format.format(today);
                String modelStartDate = format.format(model.getStartDate());
                if (modelStartDate.equals(todayString)) {
                    if (model.getDueDate().before(c.getTime())) {
                        overDueTasks.add(model);
                    } else {
                        todaysTasks.add(model);
                    }
                } else if (modelStartDate.equals(tomorrowString)) {
                    if (model.getDueDate().before(c.getTime())) {
                        overDueTasks.add(model);
                    } else {
                        tomorrowsTasks.add(model);
                    }
                } else if (model.getStartDate().after(tomorrow)) {
                    laterTasks.add(model);
                } else if (model.getStartDate().before(today)) {
                    if (model.getDueDate().before(today)) {
                        if (model.isCompleted()) {
                            pastTasks.add(model);
                        } else {
                            overDueTasks.add(model);
                        }
                    } else {
                        todaysTasks.add(model);
                    }
                }
            }
            allTasks.add(model);
        }

        //Initialize the taskModel list in the prevalent class
        Prevalent.allTasks = allTasks;
        Prevalent.pastTasks = pastTasks;
        Prevalent.overDueTasks = overDueTasks;
        Prevalent.tomorrowsTasks = tomorrowsTasks;
        Prevalent.laterDatesTasks = laterTasks;
        Prevalent.todaySTasks = todaysTasks;

        Prevalent.allNotes = new ArrayList<>();
        Prevalent.pinnedNotes = new ArrayList<>();
        Prevalent.archivedNotes = new ArrayList<>();

        RealmQuery<NoteModel> noteModelQuery = realm.where(NoteModel.class);

        //Query conditions

        //execute query
        RealmResults<NoteModel> noteResults = noteModelQuery.findAll();

        for (NoteModel noteModel : noteResults) {

            if (noteModel.getPinned() == NoteModel.PINNED) {
                Prevalent.pinnedNotes.add(noteModel);
            } else if (noteModel.getArchived() == NoteModel.ARCHIVED) {
                Prevalent.archivedNotes.add(noteModel);
            } else {
                Prevalent.allNotes.add(noteModel);
            }

        }


        Prevalent.allBudgets = new ArrayList<>();
        Prevalent.archivedBudgets = new ArrayList<>();

        RealmQuery<BudgetModel> budgetModelRealmQuery = realm.where(BudgetModel.class);

        //Query conditions

        //execute query
        RealmResults<BudgetModel> budgetModelRealmResults = budgetModelRealmQuery.findAll();

        for (BudgetModel budgetModel : budgetModelRealmResults) {
            if (budgetModel.getArchived() == BudgetModel.ARCHIVED) {
                Prevalent.archivedBudgets.add(budgetModel);
            } else {
                if (!Prevalent.allBudgets.contains(budgetModel)) {
                    Prevalent.allBudgets.add(budgetModel);

                }
            }
        }

        RealmQuery<BudgetItem> budgetItemRealmQuery = realm.where(BudgetItem.class);


        RealmResults<BudgetItem> budgetItemRealmResults = budgetItemRealmQuery.findAll();

        Prevalent.allBudgetItems.addAll(budgetItemRealmResults);

        //GOALS Section
        Prevalent.goalModels = new ArrayList<>();

        RealmQuery<GoalModel> goalModelRealmQuery = realm.where(GoalModel.class);

        RealmResults<GoalModel> goalModelRealmResults = goalModelRealmQuery.findAll();


        Prevalent.goalModels.addAll(goalModelRealmResults);
    }
}
