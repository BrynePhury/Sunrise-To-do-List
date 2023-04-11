package com.prox.sunrisetodolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.prox.sunrisetodolistapp.Adapters.TaskAdapter;
import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GoalDisplay extends AppCompatActivity {

    //View Declarations
    ImageView back_icon;
    TextView goalNameTxt;
    RecyclerView tasksRecycler;
    static ProgressBar progressBar;
    static TextView completionTxt;
    ExtendedFloatingActionButton new_task_fab;

    //Other Global variables
    int goalID;
    public static GoalModel goalModel;
    TaskAdapter adapter;
    ArrayList<TaskModel> taskModels;

    static Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_display);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        //Instantiate Views
        back_icon = findViewById(R.id.back_icon);
        goalNameTxt = findViewById(R.id.goal_name);
        tasksRecycler = findViewById(R.id.goal_recycler_view);
        completionTxt = findViewById(R.id.completion_txt);
        progressBar = findViewById(R.id.goal_progress_bar);
        new_task_fab = findViewById(R.id.new_task_fab);

        //Get goal id from intent
        goalID = getIntent().getIntExtra("goalId", -1);

        //Getting the goalModel
        getGoalModel();

        //Setting up the adapter
        taskModels = new ArrayList<>();
        adapter = new TaskAdapter(taskModels, 5);

        //Setting up recycler
        tasksRecycler.setLayoutManager(new LinearLayoutManager(this));
        tasksRecycler.setAdapter(adapter);

        //View OnClicks
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new_task_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalDisplay.this, AddTask.class);
                intent.putExtra("goalID", goalID);
                startActivity(intent);
            }
        });

    }

    public static void calculateCompletion() {

        int percentage = 0;

        int MULTIPLIER = goalModel.getPointsCount() * 100;
        if (goalModel.getTotalPoint() != 0) {
            percentage = MULTIPLIER / goalModel.getTotalPoint();
        }
        progressBar.setProgress(percentage);

        realm.beginTransaction();
        goalModel.setCompletion_percentage(percentage);
        realm.commitTransaction();

        completionTxt.setText(percentage + "% Complete");
    }

    private void getGoalModel() {
        for (GoalModel model : Prevalent.goalModels) {
            if (model.getID() == goalID) {
                goalModel = model;
                break;
            }
        }

        goalNameTxt.setText(goalModel.getGoal_name());

    }

    @Override
    protected void onStart() {
        super.onStart();
        taskModels.clear();

        for (TaskModel taskModel : Prevalent.allTasks) {
            if (taskModel.getGoalID() == goalID) {
                taskModels.add(taskModel);
                adapter.notifyDataSetChanged();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calculateCompletion();

            }
        },100);

    }
}