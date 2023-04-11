package com.prox.sunrisetodolistapp.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prox.sunrisetodolistapp.Models.BudgetItem;
import com.prox.sunrisetodolistapp.Models.BudgetModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;
import com.prox.sunrisetodolistapp.Utility.HelperClass;
import com.prox.sunrisetodolistapp.ViewBudget;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    List<BudgetModel> models;
    List<BudgetItem> shareItems = new ArrayList<>();
    Context context;
    String budgets = "";
    int callingAct;

    public static final int ARCHIVES = 100;
    public static final int ELSE = 200;

    public BudgetAdapter(List<BudgetModel> models, int callingAct) {
        this.models = models;
        this.callingAct = callingAct;
    }

    Realm realm;

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_item_layout, parent, false);
        context = parent.getContext();
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetModel budgetModel = models.get(position);

        for (BudgetItem item : Prevalent.allBudgetItems) {
            try {
                if (item.getGroupID() == budgetModel.getID()) {
                    shareItems.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < shareItems.size(); i++) {
            try {
                if (i == 0) {
                    budgets = "\n" + shareItems.get(i).getItemName() + "   K" + shareItems.get(i).getItemCost();
                } else {
                    budgets += "\n" + shareItems.get(i).getItemName() + "   K" + shareItems.get(i).getItemCost();

                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        holder.budgetNameTxt.setText(budgetModel.getBudgetName());
        holder.amount_txt.setText("Budget: K" + budgetModel.getBudget().toString());

        if (budgetModel.getCost() == null || budgetModel.getCost() == 0.0) {
            holder.cost_txt.setText("K___");

        } else {
            holder.cost_txt.setText("K" + budgetModel.getCost().toString());
        }
        if (budgetModel.getRemainder() == null || budgetModel.getRemainder() == 0.0) {
            holder.remainder_txt.setText("K___");

        } else {
            holder.remainder_txt.setText("K" + String.valueOf(budgetModel.getRemainder()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callingAct == ELSE) {
                    Intent intent = new Intent(context, ViewBudget.class);
                    intent.putExtra("budgetID", budgetModel.getID());
                    intent.putExtra("budgetName", budgetModel.getBudgetName());
                    context.startActivity(intent);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmQuery<BudgetItem> budgetItemRealmQuery = realm.where(BudgetItem.class)
                                .equalTo("groupID", budgetModel.getID());

                        RealmResults<BudgetItem> budgetItems = budgetItemRealmQuery.findAll();

                        realm.beginTransaction();
                        budgetModel.deleteFromRealm();
                        budgetItems.deleteAllFromRealm();
                        realm.commitTransaction();

                        Prevalent.allBudgets.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());

                        HelperClass.refreshTasks(context);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                CharSequence options[];
                if (budgetModel.getArchived() == BudgetModel.ARCHIVED) {
                    options = new CharSequence[]{
                            "UnArchive",
                            "Share",
                            "Delete",
                            "Edit"
                    };
                } else {
                    options = new CharSequence[]{
                            "Archive",
                            "Share",
                            "Delete",
                            "Edit"
                    };
                }
                dialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (budgetModel.getArchived() == BudgetModel.ARCHIVED) {
                                    realm.beginTransaction();
                                    budgetModel.setArchived(BudgetModel.UNARCHIVED);
                                    realm.commitTransaction();

                                    Prevalent.allBudgets.add(budgetModel);
                                    Prevalent.archivedBudgets.remove(holder.getAdapterPosition());
                                    Toast.makeText(context, "UnArchived", Toast.LENGTH_SHORT).show();

                                } else {
                                    realm.beginTransaction();
                                    budgetModel.setArchived(BudgetModel.ARCHIVED);
                                    Prevalent.archivedBudgets.add(budgetModel);
                                    realm.insertOrUpdate(budgetModel);
                                    realm.commitTransaction();

                                    Prevalent.allBudgets.remove(holder.getAdapterPosition());
                                    Toast.makeText(context, "Archived", Toast.LENGTH_SHORT).show();

                                }
                                notifyItemRemoved(holder.getAdapterPosition());
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                String title = budgetModel.getBudgetName();
                                String body = "Name: " + budgetModel.getBudgetName()
                                        + "\nCost: K"
                                        + budgetModel.getCost()
                                        + "\nAmount: K"
                                        + budgetModel.getBudget()
                                        + "\nRemainder: K"
                                        + budgetModel.getRemainder()
                                        + "\n\nITEMS"
                                        + budgets;
                                intent.putExtra(Intent.EXTRA_TEXT, title);
                                intent.putExtra(Intent.EXTRA_TEXT, body);
                                context.startActivity(Intent.createChooser(intent, "Share Budget Details"));
                                break;
                            case 2:
                                builder.show();
                                break;
                            case 3:
                                openDialog(budgetModel, holder.budgetNameTxt, holder.amount_txt);

                                break;
                        }
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    private void openDialog(BudgetModel budgetModel, TextView budgetNameTxt, TextView amount_txt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.new_budget_dialog, null, false);
        builder.setView(view);
        AlertDialog ad = builder.show();

        TextInputEditText budgetNameEt = view.findViewById(R.id.budget_name_input_et);
        TextInputEditText budgetAmountEt = view.findViewById(R.id.budget_amount_input_et);

        MaterialButton saveBtn = view.findViewById(R.id.save_budget_btn);

        budgetNameEt.setText(budgetModel.getBudgetName());
        budgetAmountEt.setText(budgetModel.getBudget().toString());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = budgetNameEt.getText().toString();
                String amt = budgetAmountEt.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    budgetNameEt.setError("Required");
                    budgetNameEt.requestFocus();

                } else if (TextUtils.isEmpty(amt)) {
                    budgetAmountEt.setError("Required");
                    budgetAmountEt.requestFocus();

                } else {
                    realm.beginTransaction();
                    budgetModel.setBudgetName(name);
                    budgetModel.setBudget(Double.valueOf(amt));
                    realm.commitTransaction();

                    budgetNameTxt.setText(name);
                    amount_txt.setText("Budget: K" + amt);
                    ad.dismiss();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView budgetNameTxt;
        TextView amount_txt;
        TextView cost_txt;
        TextView remainder_txt;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);

            budgetNameTxt = itemView.findViewById(R.id.budget_item_title);
            amount_txt = itemView.findViewById(R.id.budget_item_budget);
            cost_txt = itemView.findViewById(R.id.budget_item_cost);
            remainder_txt = itemView.findViewById(R.id.budget_item_surplus);
        }
    }
}
