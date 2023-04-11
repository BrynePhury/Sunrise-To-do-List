package com.prox.sunrisetodolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.prox.sunrisetodolistapp.Dialogs.TextSizeDialog;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Settings extends AppCompatActivity implements TextSizeDialog.TextSizeDialogListener {

    SwitchMaterial disappearingTasksSwitch;
    SwitchMaterial hideDisappearingGoals;
    TextView noteSizeTxt;

    TextSizeDialog dialog;

            Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        dialog = new TextSizeDialog();

        noteSizeTxt = findViewById(R.id.notebook_textSize);
        disappearingTasksSwitch = findViewById(R.id.disappearing_tasks_toggle);
        hideDisappearingGoals = findViewById(R.id.hide_habit_goals);

        disappearingTasksSwitch.setChecked(Prevalent.settingsModel.isDisappearingTasksEnabled());
        hideDisappearingGoals.setChecked(Prevalent.settingsModel.isHideHabitGoals());

        noteSizeTxt.setText(Prevalent.settingsModel.getNoteFontSize());

        noteSizeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(),"Text Size Dialog");
            }
        });

        disappearingTasksSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                realm.beginTransaction();
                Prevalent.settingsModel.setDisappearingTasksEnabled(isChecked);
                realm.insertOrUpdate(Prevalent.settingsModel);
                realm.commitTransaction();
            }
        });
        hideDisappearingGoals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                realm.beginTransaction();
                Prevalent.settingsModel.setDisappearingTasksEnabled(isChecked);
                realm.insertOrUpdate(Prevalent.settingsModel);
                realm.commitTransaction();
            }
        });

    }

    @Override
    public void applyTexts(String textSize) {
        realm.beginTransaction();
        Prevalent.settingsModel.setNoteFontSize(textSize);
        realm.insertOrUpdate(Prevalent.settingsModel);
        realm.commitTransaction();
        dialog.dismiss();
        noteSizeTxt.setText(textSize);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}