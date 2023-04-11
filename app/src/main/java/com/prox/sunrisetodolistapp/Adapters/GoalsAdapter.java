package com.prox.sunrisetodolistapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.sunrisetodolistapp.GoalDisplay;
import com.prox.sunrisetodolistapp.Models.GoalModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;
import com.prox.sunrisetodolistapp.Utility.HelperClass;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    List<GoalModel> goalModels;
    Context context;
    Realm realm;

    public GoalsAdapter(List<GoalModel> goalModels) {
        this.goalModels = goalModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_goal, parent, false);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GoalModel goalModel = goalModels.get(position);

        holder.goal_name.setText(goalModel.getGoal_name());

        if (goalModel.getType() == GoalModel.TYPE_HABIT) {
            holder.goal_type.setText("Habit");
            holder.goal_type.setTextSize(13);
        } else if (goalModel.getType() == GoalModel.TYPE_CHECKLIST) {
            holder.goal_type.setText(goalModel.getCompletion_percentage() + "%");


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalModel.getType() == GoalModel.TYPE_CHECKLIST) {

                    Intent intent = new Intent(context, GoalDisplay.class);
                    intent.putExtra("goalId", goalModel.getID());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Not Yet Available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                CharSequence[] options = new CharSequence[]{"Delete","Archive"};
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
                                        GoalModel model = realm.where(GoalModel.class)
                                                .equalTo("ID", goalModel.getID()).findFirst();


                                        Prevalent.goalModels.remove(goalModel);

                                        notifyItemRemoved(holder.getAdapterPosition());

                                        realm.beginTransaction();
                                        model.deleteFromRealm();
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
                                break;
                            case 1:
                                Toast.makeText(context, "Archived", Toast.LENGTH_SHORT).show();
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
        return goalModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView goal_name;
        TextView goal_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goal_name = itemView.findViewById(R.id.goal_name);
            goal_type = itemView.findViewById(R.id.goal_type);
        }
    }
}
