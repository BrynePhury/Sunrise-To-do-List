package com.prox.sunrisetodolistapp.Adapters;


import static com.prox.sunrisetodolistapp.ViewBudget.costTxt;
import static com.prox.sunrisetodolistapp.ViewBudget.remainderTxt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.sunrisetodolistapp.Dialogs.BudgetItemDialog;
import com.prox.sunrisetodolistapp.Models.BudgetItem;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;
import com.prox.sunrisetodolistapp.Utility.HelperClass;
import com.prox.sunrisetodolistapp.ViewBudget;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {
    ArrayList<BudgetItem> items;
    Context context;

    public static BudgetItemDialog budgetItemDialog;

    Realm realm;
    FragmentManager manager;

    public BudgetItemAdapter(ArrayList<BudgetItem> items, FragmentManager manager) {
        this.items = items;
        this.manager = manager;
    }

    @NonNull
    @Override
    public BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_budget, parent, false);
        context = parent.getContext();
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        return new BudgetItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemViewHolder holder, int position) {
        BudgetItem item = items.get(position);

        holder.itemPriceTxt.setText("K" + String.valueOf(item.getItemCost()));
        holder.itemNameTxt.setText(item.getItemName());
        holder.itemQtyTxt.setText("Qty: " + item.getItemQty());
        holder.itemPurchasedCheckBox.setChecked(item.isPurchased());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetItemDialog = new BudgetItemDialog(item.getID(), item.getItemName()
                        , item.getItemCost(), item.getItemQty());
                budgetItemDialog.show(manager, "Budget Item Dialog");
            }
        });
        holder.itemPurchasedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                realm.beginTransaction();
                item.setPurchased(isChecked);
                realm.commitTransaction();
            }
        });

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.card_in_animation));
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

                                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);

                                deleteDialog.setTitle("Delete Item");
                                deleteDialog.setMessage("Are you sure?");
                                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        double dub1 = Double.valueOf(costTxt.getText().toString().substring(1));

                                        double newCost = dub1 - item.getItemCost();

                                        costTxt.setText(String.valueOf(newCost));

                                        double dub2 = Double.valueOf(remainderTxt.getText().toString().substring(1));

                                        double newRemainder = dub2 + item.getItemCost();


                                        remainderTxt.setText(String.valueOf(newRemainder));

                                        BudgetItem budgetItem = ViewBudget.realm.where(BudgetItem.class)
                                                .equalTo("ID", item.getID()).findFirst();

                                        realm.beginTransaction();
                                        assert budgetItem != null;
                                        budgetItem.deleteFromRealm();
                                        realm.commitTransaction();

                                        Prevalent.allBudgetItems.remove(holder.getAdapterPosition());
                                        ViewBudget.budgetItems.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());

                                        HelperClass.refreshTasks(context);


                                    }
                                });
                                deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                deleteDialog.show();
                        }
                    }
                }).show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTxt;
        TextView itemPriceTxt;
        TextView itemQtyTxt;
        CardView cardView;
        CheckBox itemPurchasedCheckBox;

        public BudgetItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTxt = itemView.findViewById(R.id.budget_item_name);
            itemPriceTxt = itemView.findViewById(R.id.budget_item_price);
            itemQtyTxt = itemView.findViewById(R.id.budget_item_qty);
            cardView = itemView.findViewById(R.id.item_layout_budget_card);
            itemPurchasedCheckBox = itemView.findViewById(R.id.budget_item_checkbox);

        }
    }
}
