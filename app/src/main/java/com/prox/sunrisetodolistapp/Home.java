package com.prox.sunrisetodolistapp;

import static com.prox.sunrisetodolistapp.Fragments.BudgetFragment.budgetAdapter;
import static com.prox.sunrisetodolistapp.Fragments.BudgetFragment.budgetDialog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.prox.sunrisetodolistapp.Dialogs.AlarmDialog;
import com.prox.sunrisetodolistapp.Dialogs.NewBudgetDialog;
import com.prox.sunrisetodolistapp.Fragments.BudgetFragment;
import com.prox.sunrisetodolistapp.Fragments.NotesFragment;
import com.prox.sunrisetodolistapp.Fragments.ToDoListFragment;
import com.prox.sunrisetodolistapp.Models.BudgetModel;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.Receiver.AlertReceiver;

import java.util.Calendar;

import io.realm.Realm;

public class Home extends AppCompatActivity implements NewBudgetDialog.NewBudgetDialogListener, AlarmDialog.AlarmDialogListener {

    //View Declarations
    BottomNavigationView bottomNav;
    ImageView tool_bar_menu;
    DrawerLayout drawerLayout;

    //Constants

    //Other Variables
    public static int TASKID = -1;
    public static String TaskName = "";
    Realm realm;

    AlarmDialog alarmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        boolean isNote = intent.getBooleanExtra("isNote", false);

        bottomNav = findViewById(R.id.bottom_navigation_bar);
        tool_bar_menu = findViewById(R.id.tool_bar_archives);
        drawerLayout = findViewById(R.id.drawer_layout);


        //Setting default fragment
        if (isNote) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new NotesFragment()).commit();

            //Selecting default selection on navigation bar
            bottomNav.setSelectedItemId(R.id.bottom_nav_notes);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ToDoListFragment()).commit();

            //Selecting default selection on navigation bar
            bottomNav.setSelectedItemId(R.id.bottom_nav_todo_list);
        }

        BudgetFragment budgetFragment = new BudgetFragment();
        NotesFragment notesFragment = new NotesFragment();
        ToDoListFragment toDoListFragment = new ToDoListFragment();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_budget:
                        //load summary fragment
                        fragment = budgetFragment;
                        break;

                    case R.id.bottom_nav_notes:
                        //load notes fragment
                        fragment = notesFragment;
                        break;

                    case R.id.bottom_nav_todo_list:
                        //load to do list fragment
                        fragment = toDoListFragment;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

                return true;
            }
        });


        tool_bar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        AlertReceiver.finished.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    alarmDialog = new AlarmDialog(TaskName, TASKID);

                    alarmDialog.show(getSupportFragmentManager(), "Alarm Dialog");
                }
            }
        });
    }

    @Override
    public void applyTexts(String budgetName, double amount) {

        BudgetModel budgetModel = new BudgetModel();
        budgetModel.setBudgetName(budgetName);
        budgetModel.setBudget(amount);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(BudgetModel.class).max("ID");
                int nextId;
                if (currentIdNum == null) {

                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                budgetModel.setID(nextId);
                realm.insertOrUpdate(budgetModel);

                Prevalent.allBudgets.add(budgetModel);

            }
        });
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        budgetAdapter.notifyDataSetChanged();
        budgetDialog.dismiss();
        BudgetFragment.budget_label.setVisibility(View.VISIBLE);
    }

    @Override
    public void extendTask(int id, int extension) {
        TaskModel tModel = realm.where(TaskModel.class)
                .equalTo("id", id).findFirst();
        Calendar c = Calendar.getInstance();

        c.setTime(tModel.getDueTime());

        if (extension == TaskModel.ONE_DAY_EXTENSION) {
            c.add(Calendar.DAY_OF_MONTH,1);

            realm.beginTransaction();
            tModel.setDueDate(c.getTime());
            tModel.setDueTime(c.getTime());
            realm.commitTransaction();

            setDueAlarm("High","",tModel,c);

            alarmDialog.dismiss();
            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

        } else if (extension == TaskModel.ONE_WEEK_EXTENSION) {
            c.add(Calendar.WEEK_OF_MONTH,1);

            realm.beginTransaction();
            tModel.setDueDate(c.getTime());
            tModel.setDueTime(c.getTime());
            realm.commitTransaction();

            setDueAlarm("High","",tModel,c);

            alarmDialog.dismiss();
            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

        } else if (extension == TaskModel.ONE_MONTH_EXTENSION) {
            c.add(Calendar.MONTH,1);

            realm.beginTransaction();
            tModel.setDueDate(c.getTime());
            tModel.setDueTime(c.getTime());
            realm.commitTransaction();

            setDueAlarm("High","",tModel,c);

            alarmDialog.dismiss();
            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

        }

    }

    private void setDueAlarm(String priorityLevel, String desc, TaskModel taskModel, Calendar c) {

        if (priorityLevel.equals("High")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("title", taskModel.getTaskName());
            intent.putExtra("description", desc);
            intent.putExtra("checker", 3);
            intent.putExtra("id",taskModel.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskModel.getId()+3000, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

            }

        }
    }

    public void clickHome(View view){
        closeDrawer(drawerLayout);
    }

    public void clickGoals(View view){
        startActivity(new Intent(Home.this, Goals.class));
        closeDrawer(drawerLayout);
    }

    public void clickSettings(View view){
        startActivity(new Intent(Home.this, Settings.class));

    }

    public void clickArchives(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

        CharSequence[] options = new CharSequence[]{
                "Budget Archives",
                "Note Archives"
        };
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Home.this, Archives.class);

                switch (which) {
                    case 0:
                        intent.putExtra("archive", "Budgets");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("archive", "Notes");
                        startActivity(intent);
                        break;
                }

            }
        });
        closeDrawer(drawerLayout);
        builder.show();
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);

    }

    private static void closeDrawer(DrawerLayout drawerLayout) {

        //Close drawer layout
        //Check if open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }




}