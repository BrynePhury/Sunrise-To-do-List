package com.prox.sunrisetodolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.prox.sunrisetodolistapp.Adapters.GoalsAdapter;
import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import java.util.ArrayList;
import java.util.List;

public class Goals extends AppCompatActivity {

    //View Declarations
    ImageView back_icon;
    RecyclerView goals_recycler;
    ExtendedFloatingActionButton new_goal_fab;
    TextView nothing_txt;
    ImageView nothing_img;

    //Other Variables
    GoalsAdapter adapter;
    ArrayList<GoalModel> goalModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        back_icon = findViewById(R.id.goals_back);
        goals_recycler = findViewById(R.id.goals_recycler_view);
        new_goal_fab = findViewById(R.id.new_goal_fab);
        nothing_img = findViewById(R.id.nothing_img);
        nothing_txt = findViewById(R.id.nothing_txt);

        adapter = new GoalsAdapter(Prevalent.goalModels);

        goals_recycler.setLayoutManager(new LinearLayoutManager(this));
        goals_recycler.setAdapter(adapter);
        goals_recycler.setHasFixedSize(true);



        //View onClickListeners
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new_goal_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Goals.this,AddNewGoal.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Prevalent.goalModels.isEmpty()){
            nothing_img.setVisibility(View.VISIBLE);
            nothing_txt.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}