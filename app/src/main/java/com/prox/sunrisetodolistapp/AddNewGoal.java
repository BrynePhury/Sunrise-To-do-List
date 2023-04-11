package com.prox.sunrisetodolistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddNewGoal extends AppCompatActivity implements View.OnClickListener {

    //View Declarations
    ImageView back_icon;
    TextInputEditText goal_name_input;
    TextInputEditText goal_notes_input;
    TextInputEditText goal_end_date_display;
    TextInputEditText daily_time_display;
    RadioGroup goal_type_radio_group;
    LinearLayout selectDaysLayout;
    LinearLayout daily_time_layout;
    MaterialButton set_end_date_button;
    MaterialButton set_daily_time_btn;
    MaterialButton set_goal_btn;
    CheckBox sun_check, mon_check, tue_check, wed_check, thur_check, fri_check, sat_check;

    //Date Calenders to store time
    Calendar endCalender;

    //Other Variables
    private GoalModel goalModel;
    private Realm realm;

    boolean dateSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_goal);
        //Setting up realm configurations
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        //initializing goal model
        goalModel = new GoalModel();

        //setting default type for the goal
        goalModel.setType(GoalModel.TYPE_CHECKLIST);

        //Views Initialized
        back_icon = findViewById(R.id.back_icon);
        goal_name_input = findViewById(R.id.goal_name_input);
        goal_notes_input = findViewById(R.id.goal_notes_input);
        goal_type_radio_group = findViewById(R.id.radioGroup);
        selectDaysLayout = findViewById(R.id.selectDaysLayout);
        daily_time_layout = findViewById(R.id.daily_time_layout);
        set_end_date_button = findViewById(R.id.set_end_date_button);
        set_daily_time_btn = findViewById(R.id.set_daily_time_btn);
        goal_end_date_display = findViewById(R.id.goal_end_date_display);
        daily_time_display = findViewById(R.id.daily_time_display);
        set_goal_btn = findViewById(R.id.set_goal_btn);
        sun_check = findViewById(R.id.sun_check);
        mon_check = findViewById(R.id.mon_check);
        tue_check = findViewById(R.id.tue_check);
        wed_check = findViewById(R.id.wed_check);
        thur_check = findViewById(R.id.thur_check);
        fri_check = findViewById(R.id.fri_check);
        sat_check = findViewById(R.id.sat_check);

        //Creating a calender instance for the end date
        endCalender = Calendar.getInstance();

        //Using the calender instance we can get the current
        //day so as to display it on the date picker
        int year = endCalender.get(Calendar.YEAR);
        int day = endCalender.get(Calendar.DAY_OF_MONTH);
        int month = endCalender.get(Calendar.MONTH);

        //View Listeners
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        goal_type_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.checklist_button:
                        //hide unnecessary views 
                        selectDaysLayout.setVisibility(View.GONE);
                        daily_time_layout.setVisibility(View.GONE);

                        //Set goal type
                        goalModel.setType(GoalModel.TYPE_CHECKLIST);

                        break;
                    case R.id.habit_button:
                        //show necessary views
                        selectDaysLayout.setVisibility(View.VISIBLE);
                        daily_time_layout.setVisibility(View.VISIBLE);

                        //Set goal type
                        goalModel.setType(GoalModel.TYPE_HABIT);
                }
            }
        });

        sun_check.setOnClickListener(this);
        mon_check.setOnClickListener(this);
        tue_check.setOnClickListener(this);
        wed_check.setOnClickListener(this);
        thur_check.setOnClickListener(this);
        fri_check.setOnClickListener(this);
        sat_check.setOnClickListener(this);


        set_end_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddNewGoal.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //saving the selected date
                                endCalender = Calendar.getInstance();
                                endCalender.set(Calendar.YEAR, year);
                                endCalender.set(Calendar.MONTH, month);
                                endCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                endCalender.set(Calendar.HOUR_OF_DAY, 0);
                                endCalender.set(Calendar.MINUTE, 0);
                                endCalender.set(Calendar.SECOND, 0);
                                Date date = endCalender.getTime();


                                //formatting and displaying the selected date
                                SimpleDateFormat format = new SimpleDateFormat("dd MMM,yyyy", Locale.getDefault());
                                String formattedDate = format.format(date);
                                goal_end_date_display.setText(formattedDate);

                                realm.beginTransaction();
                                //Set task start date
                                goalModel.setCompletion_date(date);
                                realm.commitTransaction();

                                //set variable date selected to true
                                dateSelected = true;

                            }
                        }, year, month, day
                );
                datePickerDialog.show();
            }
        });

        set_daily_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddNewGoal.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Making an instance of calender to save the chosen time
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                endCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endCalender.set(Calendar.MINUTE, minute);
                                Date time = endCalender.getTime();

                                //Formatting and displaying the time
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String dueTime = format.format(time);
                                daily_time_display.setText(dueTime);

                                realm.beginTransaction();
                                //Set task due time
                                goalModel.setDaily_time(time);
                                realm.commitTransaction();

                            }
                        }, 0, 1, true
                );
                timePickerDialog.show();
            }
        });

        set_goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalName = goal_name_input.getText().toString();

                if (goalName.isEmpty()) {
                    goal_name_input.setError("Required");
                    goal_name_input.requestFocus();

                } else if (!dateSelected) {
                    Toast.makeText(AddNewGoal.this, "Select Target Completion date", Toast.LENGTH_SHORT).show();

                } else {
                    goalModel.setGoal_name(goalName);

                    saveGoal();

                }
            }
        });

    }

    private void saveGoal() {

        //Save to Realm
        realm.beginTransaction();

        if (true) {
            //Create item id
            Number currentIdNum = realm.where(GoalModel.class).max("ID");
            int nextId;
            if (currentIdNum == null) {

                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            goalModel.setID(nextId);


        }
        Prevalent.goalModels.add(goalModel);
        realm.insertOrUpdate(goalModel);

        realm.commitTransaction();

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sun_check:
                goalModel.setSunday(sun_check.isChecked());
                break;
            case R.id.mon_check:
                goalModel.setMonday(mon_check.isChecked());

                break;
            case R.id.tue_check:
                goalModel.setTuesday(tue_check.isChecked());
                break;
            case R.id.wed_check:
                goalModel.setWednesday(wed_check.isChecked());
                break;
            case R.id.thur_check:
                goalModel.setThursday(thur_check.isChecked());
                break;
            case R.id.fri_check:
                goalModel.setFriday(fri_check.isChecked());
                break;
            case R.id.sat_check:
                goalModel.setSaturday(sat_check.isChecked());
                break;
        }

    }
}