package com.prox.sunrisetodolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.sunrisetodolistapp.Adapters.BudgetAdapter;
import com.prox.sunrisetodolistapp.Adapters.NoteAdapter;
import com.prox.sunrisetodolistapp.Models.BudgetModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import java.util.ArrayList;

import io.realm.Realm;

public class Archives extends AppCompatActivity {

    TextView archives_title;
    ImageView archives_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);

        Intent intent = getIntent();

        String title = intent.getStringExtra("archive");

        archives_back = findViewById(R.id.archives_back);
        archives_title = findViewById(R.id.archives_title);
        archives_title.setText(title);

        RecyclerView archiveRecycler = findViewById(R.id.archive_recycler_view);
        archiveRecycler.setHasFixedSize(true);
        archiveRecycler.setLayoutManager(new LinearLayoutManager(this));

        archives_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (title.equals("Budgets")) {
            archiveRecycler.setAdapter(new BudgetAdapter(Prevalent.archivedBudgets, BudgetAdapter.ARCHIVES));
        } else if (title.equals("Notes")) {
            archiveRecycler.setAdapter(new NoteAdapter(Prevalent.archivedNotes, NoteAdapter.ARCHIVES));

        }


    }
}