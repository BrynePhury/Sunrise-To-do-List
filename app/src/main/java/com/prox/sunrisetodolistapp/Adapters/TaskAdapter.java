package com.prox.sunrisetodolistapp.Adapters;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.sunrisetodolistapp.AddTask;
import com.prox.sunrisetodolistapp.Fragments.ToDoListFragment;
import com.prox.sunrisetodolistapp.GoalDisplay;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;
import com.prox.sunrisetodolistapp.Receiver.AlertReceiver;
import com.prox.sunrisetodolistapp.Utility.HelperClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public static final int OVERDUE_TASK = 0;
    public static final int TODAY_TASK = 1;
    public static final int TOMORROW_TASK = 2;
    public static final int LATER_TASK = 3;
    public static final int GOALS_TASK = 5;

    ArrayList<TaskModel> models;
    Context context;
    Realm realm;
    int task;

    public TaskAdapter(ArrayList<TaskModel> models, int task) {
        this.models = models;
        this.task = task;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate task item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_layout, parent, false);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        //get context from parent
        context = parent.getContext();

        //Return new task view holder with the inflated layout
        return new TaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        //Get the task in question from the list using the position
        TaskModel model = models.get(position);

        if (model.isCompleted()) {
            holder.taskCheckbox.setChecked(true);
        }

        if (task == OVERDUE_TASK) {
            holder.taskStateDisplay.setVisibility(View.VISIBLE);
        } else {
            holder.taskStateDisplay.setVisibility(View.GONE);

        }

        //Set up the task to be displayed
        holder.taskNameDisplay.setText(model.getTaskName());
        holder.taskCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                model.setCompleted(!model.isCompleted());
                realm.commitTransaction();
                if (task == OVERDUE_TASK) {
                    Prevalent.overDueTasks.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }

                if (model.getPriorityLevel().equals("High")) {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlertReceiver.class);
                    intent.putExtra("title", model.getTaskName());
                    intent.putExtra("description", "");
                    intent.putExtra("checker", 3);
                    intent.putExtra("id", model.getId());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, model.getId() + 3000, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    if (!model.isCompleted()) {


                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, model.getDueTime().getTime(), pendingIntent);
                        } else {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, model.getDueTime().getTime(), pendingIntent);

                        }
                    } else {
                        alarmManager.cancel(pendingIntent);
                    }
                }
                if (Prevalent.settingsModel.isDisappearingTasksEnabled()) {
                    try {
                        if (task == TODAY_TASK) {
                            ToDoListFragment.filteredTodayTasks.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        } else if (task == TOMORROW_TASK) {
                            ToDoListFragment.filteredTomorrowTasks.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        } else if (task == LATER_TASK) {
                            ToDoListFragment.filteredLaterTasks.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
                if (task == GOALS_TASK) {
                    if (model.getPriorityLevel().equals("Low")) {
                        if (model.isCompleted()) {
                            GoalDisplay.goalModel.setPointsCount(GoalDisplay.goalModel.getPointsCount() + 1);
                        } else {
                            GoalDisplay.goalModel.setPointsCount(GoalDisplay.goalModel.getPointsCount() - 1);

                        }
                    } else if (model.getPriorityLevel().equals("Moderate")) {
                        if (model.isCompleted()) {
                            GoalDisplay.goalModel.setPointsCount(GoalDisplay.goalModel.getPointsCount() + 2);
                        } else {
                            GoalDisplay.goalModel.setPointsCount(GoalDisplay.goalModel.getPointsCount() - 2);

                        }
                    } else if (model.getPriorityLevel().equals("High")) {
                        if (model.isCompleted()) {
                            GoalDisplay.goalModel.setPointsCount(GoalDisplay.goalModel.getPointsCount() + 3);
                        } else {
                            GoalDisplay.goalModel.setPointsCount(GoalDisplay.goalModel.getPointsCount() - 3);

                        }
                    }
                    GoalDisplay.calculateCompletion();
                }
            }
        });
        if (model.getStartTime() != null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);
            holder.taskTimeDisplay.setText(format.format(model.getStartTime()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTask.class);
                intent.putExtra("act", 200);
                intent.putExtra("id", model.getId());
                intent.putExtra("task", task);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                CharSequence[] options = new CharSequence[]{"Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AlertDialog.Builder d = new AlertDialog.Builder(context);

                                d.setTitle("Delete Item");
                                d.setMessage("Are you sure?");
                                d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TaskModel tModel = realm.where(TaskModel.class)
                                                .equalTo("id", model.getId()).findFirst();


                                        if (Prevalent.settingsModel.isDisappearingTasksEnabled()) {
                                            if (task == TODAY_TASK) {
                                                ToDoListFragment.filteredTodayTasks.remove(model);
                                                if (ToDoListFragment.filteredTodayTasks.size() < 1) {
                                                    ToDoListFragment.todayLabel.setVisibility(View.GONE);
                                                    ToDoListFragment.todayRecyclerView.setVisibility(View.GONE);
                                                }
                                                notifyItemRemoved(holder.getAdapterPosition());
                                            } else if (task == TOMORROW_TASK) {
                                                ToDoListFragment.filteredTomorrowTasks.remove(model);
                                                if (ToDoListFragment.filteredTomorrowTasks.size() < 1) {
                                                    ToDoListFragment.tomorrowLabel.setVisibility(View.GONE);
                                                    ToDoListFragment.tomorrowRecyclerView.setVisibility(View.GONE);
                                                }
                                                notifyItemRemoved(holder.getAdapterPosition());
                                            } else if (task == LATER_TASK) {
                                                ToDoListFragment.filteredLaterTasks.remove(model);
                                                if (ToDoListFragment.filteredLaterTasks.size() < 1) {
                                                    ToDoListFragment.laterLabel.setVisibility(View.GONE);
                                                    ToDoListFragment.laterRecyclerView.setVisibility(View.GONE);
                                                }
                                                notifyItemRemoved(holder.getAdapterPosition());
                                            }
                                        }
                                        if (task == TODAY_TASK) {
                                            Prevalent.todaySTasks.remove(model);
                                            if (Prevalent.todaySTasks.size() < 1) {
                                                ToDoListFragment.todayLabel.setVisibility(View.GONE);
                                                ToDoListFragment.todayRecyclerView.setVisibility(View.GONE);
                                            }
                                            notifyItemRemoved(holder.getAdapterPosition());
                                        } else if (task == TOMORROW_TASK) {
                                            Prevalent.tomorrowsTasks.remove(model);
                                            if (Prevalent.tomorrowsTasks.size() < 1) {
                                                ToDoListFragment.tomorrowLabel.setVisibility(View.GONE);
                                                ToDoListFragment.tomorrowRecyclerView.setVisibility(View.GONE);
                                            }
                                            notifyItemRemoved(holder.getAdapterPosition());
                                        } else if (task == LATER_TASK) {
                                            Prevalent.laterDatesTasks.remove(model);
                                            if (Prevalent.laterDatesTasks.size() < 1) {
                                                ToDoListFragment.laterLabel.setVisibility(View.GONE);
                                                ToDoListFragment.laterRecyclerView.setVisibility(View.GONE);
                                            }
                                            notifyItemRemoved(holder.getAdapterPosition());
                                        } else if (task == OVERDUE_TASK) {
                                            Prevalent.overDueTasks.remove(model);
                                            if (Prevalent.overDueTasks.size() < 1) {
                                                ToDoListFragment.overdueLabel.setVisibility(View.GONE);
                                                ToDoListFragment.overdueRecyclerView.setVisibility(View.GONE);
                                            }
                                            notifyItemRemoved(holder.getAdapterPosition());
                                        }

                                        realm.beginTransaction();
                                        tModel.deleteFromRealm();
                                        realm.commitTransaction();
                                        HelperClass.refreshTasks(context);

                                    }
                                });
                                d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                d.show();
                        }
                    }
                });
                builder.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        //Declare item views
        CheckBox taskCheckbox;
        TextView taskNameDisplay;
        TextView taskStateDisplay;
        TextView taskTimeDisplay;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            //Finding the item views
            taskCheckbox = itemView.findViewById(R.id.task_item_checkbox);
            taskNameDisplay = itemView.findViewById(R.id.task_item_name);
            taskStateDisplay = itemView.findViewById(R.id.task_item_state);
            taskTimeDisplay = itemView.findViewById(R.id.task_item_time);
        }
    }
}
