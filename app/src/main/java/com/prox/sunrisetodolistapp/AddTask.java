package com.prox.sunrisetodolistapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prox.sunrisetodolistapp.Adapters.TaskAdapter;
import com.prox.sunrisetodolistapp.Fragments.ToDoListFragment;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.Receiver.AlertReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddTask extends AppCompatActivity {

    //View Declarations
    private TextInputEditText taskNameInput;
    private TextInputEditText taskDescriptionInput;
    private TextInputEditText dueDateDisplay;
    private TextInputEditText startDateDisplay;
    private MaterialButton setDueDateBtn;
    private MaterialButton setStartDateBtn;
    private MaterialButton setDueTimeBtn;
    private MaterialButton setStartTimeBtn;
    private LinearLayout dueTimeLayout;
    private LinearLayout startTimeLayout;
    private TextView dueTimeTv;
    private TextView startTimeTv, add_task_text;
    private ImageView actionBackIcon;
    private MaterialButton addTaskBtn;
    private Spinner priorityLevelSpinner;
    private String selectedDate;

    int act;

    Calendar startCalender;
    Calendar dueCalender;

    //Constants

    //Other Variables
    private TaskModel taskModel;
    private Realm realm;

    int goalId;
    int id;
    boolean isDraft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        //Finding the views
        add_task_text = findViewById(R.id.add_task_text);
        taskNameInput = findViewById(R.id.task_name_input_et);
        taskDescriptionInput = findViewById(R.id.task_description_input_et);
        dueDateDisplay = findViewById(R.id.task_due_date_et);
        startDateDisplay = findViewById(R.id.task_start_date_input_et);
        setDueDateBtn = findViewById(R.id.set_due_date_btn);
        setStartDateBtn = findViewById(R.id.set_start_date_btn);
        dueTimeLayout = findViewById(R.id.task_due_time_layout);
        startTimeLayout = findViewById(R.id.task_start_time_layout);
        setStartTimeBtn = findViewById(R.id.task_start_time_btn);
        setDueTimeBtn = findViewById(R.id.task_due_time_btn);
        dueTimeTv = findViewById(R.id.task_due_time_tv);
        startTimeTv = findViewById(R.id.task_start_time_tv);
        actionBackIcon = findViewById(R.id.add_task_back);
        addTaskBtn = findViewById(R.id.add_task_btn);
        priorityLevelSpinner = findViewById(R.id.add_task_priority_level_spinner);

        act = getIntent().getIntExtra("act", -1);
        id = getIntent().getIntExtra("id", -1);
        int task = getIntent().getIntExtra("task", -1);
        goalId = getIntent().getIntExtra("goalID", -1);


        //Instantiating variables
        if (act == 200 && id != -1) {
            for (TaskModel model : Prevalent.allTasks) {
                if (model.getId() == id) {
                    taskModel = model;
                }
            }
        } else {

            if (!Prevalent.allTasks.isEmpty()) {

                for (TaskModel model : Prevalent.allTasks) {

                    if (model.isDraft()) {
                        try {
                        taskModel = model;
                        loadDraft(model);
                        Prevalent.allTasks.remove(model);
                        break;
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        taskModel = new TaskModel();

                    }
                }
            } else {
                taskModel = new TaskModel();

            }
        }

        if (goalId != -1) {
            realm.beginTransaction();
            taskModel.setGoalID(goalId);
            realm.commitTransaction();
        }

        //Getting current date constants
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        if (act == 200) {
            if (task == TaskAdapter.OVERDUE_TASK) {
                Prevalent.overDueTasks.remove(taskModel);
            } else if (task == TaskAdapter.TODAY_TASK) {
                Prevalent.todaySTasks.remove(taskModel);
            } else if (task == TaskAdapter.TOMORROW_TASK) {
                Prevalent.tomorrowsTasks.remove(taskModel);
            } else if (task == TaskAdapter.LATER_TASK) {
                Prevalent.laterDatesTasks.remove(taskModel);
            }
            add_task_text.setVisibility(View.INVISIBLE);
            taskNameInput.setText(taskModel.getTaskName());
            taskDescriptionInput.setText(taskModel.getTaskDescription());
            SimpleDateFormat format = new SimpleDateFormat("dd MMM,yyyy", Locale.getDefault());
            String formattedStartDate = format.format(taskModel.getStartDate());

            startCalender = Calendar.getInstance();
            startCalender.setTime(taskModel.getStartTime());
            startDateDisplay.setText(formattedStartDate);

            dueCalender = Calendar.getInstance();
            dueCalender.setTime(taskModel.getDueDate());

            String formattedDueDate = format.format(taskModel.getDueDate());
            dueDateDisplay.setText(formattedDueDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dueTime = timeFormat.format(taskModel.getDueTime());
            dueTimeTv.setText(dueTime);
            dueTimeLayout.setVisibility(View.VISIBLE);
            String startTime = timeFormat.format(taskModel.getStartTime());
            startTimeTv.setText(startTime);
            startTimeLayout.setVisibility(View.VISIBLE);
            addTaskBtn.setText("Save Task");

        } else {
            dueCalender = Calendar.getInstance();
        }

        //Setting up onclick listeners
        setDueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //Making a calender instance to save the picked date
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.HOUR_OF_DAY, 0);
                                c.set(Calendar.MINUTE, 0);
                                c.set(Calendar.SECOND, 0);
                                Date date = c.getTime();
                                dueCalender.set(Calendar.YEAR, year);
                                dueCalender.set(Calendar.MONTH, month);
                                dueCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                //formatting and displaying the selected date
                                SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                                String formattedDate = format.format(date);
                                dueDateDisplay.setText(formattedDate);


                                realm.beginTransaction();
                                //Set task due date
                                taskModel.setDueDate(date);
                                realm.commitTransaction();

                                //Showing the set time layout
                                dueTimeLayout.setVisibility(View.VISIBLE);


                            }

                        }, year, month, day
                );
                datePickerDialog.show();
            }
        });

        setStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //Making a calender instance to save the picked date
                                startCalender = Calendar.getInstance();
                                startCalender.set(Calendar.YEAR, year);
                                startCalender.set(Calendar.MONTH, month);
                                startCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                startCalender.set(Calendar.HOUR_OF_DAY, 0);
                                startCalender.set(Calendar.MINUTE, 0);
                                startCalender.set(Calendar.SECOND, 0);
                                Date date = startCalender.getTime();

                                //Check if date is not before current date

                                //formatting and displaying the selected date
                                SimpleDateFormat format = new SimpleDateFormat("dd MMM,yyyy", Locale.getDefault());
                                String formattedDate = format.format(date);
                                startDateDisplay.setText(formattedDate);

                                //Set selected start Date string
                                selectedDate = formattedDate;

                                realm.beginTransaction();
                                //Set task start date
                                taskModel.setStartDate(date);
                                realm.commitTransaction();

                                //Showing the set time layout
                                startTimeLayout.setVisibility(View.VISIBLE);


                            }
                        }, year, month, day
                );
                datePickerDialog.show();
            }
        });

        setDueTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTask.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Making an instance of calender to save the chosen time
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                dueCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                dueCalender.set(Calendar.MINUTE, minute);
                                Date time = dueCalender.getTime();

                                //Formatting and displaying the time
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String dueTime = format.format(time);
                                dueTimeTv.setText(dueTime);

                                realm.beginTransaction();
                                //Set task due time
                                taskModel.setDueTime(time);
                                realm.commitTransaction();

                            }
                        }, 0, 1, true
                );
                timePickerDialog.show();
            }
        });

        setStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTask.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Making a calender instance to save the time
                                startCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startCalender.set(Calendar.MINUTE, minute);
                                Date time = startCalender.getTime();

                                //Formatting and displaying the time
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);
                                String startTime = format.format(time);
                                startTimeTv.setText(startTime);

                                realm.beginTransaction();
                                taskModel.setStartTime(time);
                                realm.commitTransaction();

                            }
                        }, hour, minute, true
                );
                timePickerDialog.show();
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        actionBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        priorityLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (taskModel != null) {
                    realm.beginTransaction();
                    taskModel.setPriorityLevel(priorityLevelSpinner.getSelectedItem().toString());
                    realm.commitTransaction();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (taskModel != null) {
                    realm.beginTransaction();
                    taskModel.setPriorityLevel(priorityLevelSpinner.getSelectedItem().toString());
                    realm.commitTransaction();
                }
            }
        });

    }

    private void loadDraft(TaskModel model) {
        taskNameInput.setText(model.getTaskName());
        taskDescriptionInput.setText(model.getTaskDescription());

        //formatting and displaying the selected date
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String formattedDate;
        if (model.getDueDate() != null) {
            formattedDate = format.format(model.getDueDate());
            dueDateDisplay.setText(formattedDate);
        }
        if (model.getStartDate() != null) {
            formattedDate = format.format(model.getStartDate());
            startDateDisplay.setText(formattedDate);
        }

        SimpleDateFormat f = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time;
        if (model.getDueTime() != null) {
            time = f.format(model.getDueTime());
            dueTimeTv.setText(time);
        }
        if (model.getStartTime() != null) {
            time = f.format(model.getStartTime());
            startTimeTv.setText(time);
        }
        switch (model.getPriorityLevel()) {
            case "Low":
                priorityLevelSpinner.setSelection(1);
                break;
            case "Moderate":
                priorityLevelSpinner.setSelection(2);
                break;
            case "High":
                priorityLevelSpinner.setSelection(3);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (goalId != -1) {
            GoalDisplay.realm.beginTransaction();
            if (taskModel.getPriorityLevel().equals("Low")) {
                GoalDisplay.goalModel.setTotalPoint(GoalDisplay.goalModel.getTotalPoint() + 1);
            } else if (taskModel.getPriorityLevel().equals("Moderate")) {
                GoalDisplay.goalModel.setTotalPoint(GoalDisplay.goalModel.getTotalPoint() + 2);
            } else if (taskModel.getPriorityLevel().equals("High")) {
                GoalDisplay.goalModel.setTotalPoint(GoalDisplay.goalModel.getTotalPoint() + 3);
            }
            GoalDisplay.realm.commitTransaction();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        } else if (id != -1 || act != -1) {

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        } else if (isDraft) {
            Toast.makeText(this, "Saved as draft", Toast.LENGTH_SHORT).show();
        }
    }


    private void addTask() {
        //getting input from EditTexts
        String taskName = taskNameInput.getText().toString();
        String taskDescription = taskDescriptionInput.getText().toString();

        if (act == -1) {
            //Save task priority level
            realm.beginTransaction();
            taskModel.setPriorityLevel(priorityLevelSpinner.getSelectedItem().toString());
            realm.commitTransaction();
        }

        //Validate the input
        if (TextUtils.isEmpty(taskName)) {
            isDraft = true;
            saveDraft();

        } else if (taskModel.getDueDate() == null) {
            isDraft = true;
            saveDraft();

        } else if (taskModel.getStartTime() == null) {
            isDraft = true;
            saveDraft();

        } else if (taskModel.getDueTime() == null) {
            isDraft = true;
            saveDraft();

        } else if (taskModel.getStartDate() == null) {
            isDraft = true;
            saveDraft();

        } else {

            realm.beginTransaction();
            //Set the task name and description
            taskModel.setTaskName(taskName);
            taskModel.setTaskDescription(taskDescription);
            realm.commitTransaction();

            //Save the task
            saveTask();

            //Set Alarm
            setAlarm(taskModel.getPriorityLevel(), "You have a task to get started");
            setDueAlarm(taskModel.getPriorityLevel(), "Due Task");
        }
    }

    private void setDueAlarm(String priorityLevel, String desc) {

        if (priorityLevel.equals("High")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("title", taskModel.getTaskName());
            intent.putExtra("description", desc);
            intent.putExtra("checker", 3);
            intent.putExtra("id", taskModel.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskModel.getId() + 3000, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, dueCalender.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dueCalender.getTimeInMillis(), pendingIntent);

            }

        }
    }

    private void setAlarm(String priorityLevel, String description) {
        if (priorityLevel.equals("High")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("title", taskModel.getTaskName());
            intent.putExtra("description", description);
            intent.putExtra("checker", 2);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskModel.getId(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            startCalender.add(Calendar.MINUTE, -5);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startCalender.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startCalender.getTimeInMillis(), pendingIntent);

            }

        } else if (priorityLevel.equals("Moderate")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("title", taskModel.getTaskName());
            intent.putExtra("description", description);
            intent.putExtra("checker", 2);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskModel.getId(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startCalender.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startCalender.getTimeInMillis(), pendingIntent);

            }
        }
    }

    private void saveTask() {
        //Save to Realm
        realm.beginTransaction();

        taskModel.setDraft(false);

        if (act == -1) {
            //Create item id
            Number currentIdNum = realm.where(TaskModel.class).max("id");
            int nextId;
            if (currentIdNum == null) {

                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            taskModel.setId(nextId);


        }
        realm.insertOrUpdate(taskModel);

        realm.commitTransaction();


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

        SimpleDateFormat format = new SimpleDateFormat("dd,MM,yyyy", Locale.UK);
        String todayString = format.format(today);
        String tomorrowString = format.format(tomorrow);


        String modelStartDate = format.format(taskModel.getStartDate());
        if (modelStartDate.equals(todayString)) {
            Prevalent.todaySTasks.add(taskModel);
        } else if (modelStartDate.equals(tomorrowString)) {
            Prevalent.tomorrowsTasks.add(taskModel);
        } else if (taskModel.getStartDate().after(tomorrow)) {
            Prevalent.laterDatesTasks.add(taskModel);
        } else if (taskModel.getStartDate().before(today)) {
            if (taskModel.getDueDate().before(today)) {

                Prevalent.overDueTasks.add(taskModel);
            } else {
                Prevalent.todaySTasks.add(taskModel);
            }
        }

        ToDoListFragment.todayAdapter.notifyDataSetChanged();
        ToDoListFragment.tomorrowAdapter.notifyDataSetChanged();
        ToDoListFragment.laterAdapter.notifyDataSetChanged();
        ToDoListFragment.overDueAdapter.notifyDataSetChanged();

        //Add task to the prevalent list
        Prevalent.allTasks.add(taskModel);

        //Start previous activity
        onBackPressed();

    }

    private void saveDraft() {
        realm.beginTransaction();
        taskModel.setTaskName(taskNameInput.getText().toString());
        taskModel.setTaskDescription(taskDescriptionInput.getText().toString());
        taskModel.setDraft(true);
        realm.insertOrUpdate(taskModel);
        realm.commitTransaction();

        Prevalent.allTasks.add(taskModel);

        onBackPressed();

    }
}