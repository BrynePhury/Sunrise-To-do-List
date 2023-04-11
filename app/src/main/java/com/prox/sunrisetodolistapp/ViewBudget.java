package com.prox.sunrisetodolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prox.sunrisetodolistapp.Adapters.BudgetItemAdapter;
import com.prox.sunrisetodolistapp.Dialogs.BudgetItemDialog;
import com.prox.sunrisetodolistapp.Fragments.BudgetFragment;
import com.prox.sunrisetodolistapp.Models.BudgetItem;
import com.prox.sunrisetodolistapp.Models.BudgetModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ViewBudget extends AppCompatActivity implements BudgetItemDialog.BudgetItemListener {

    TextView actionTxt;
    public static TextView costTxt;
    public static TextView remainderTxt;
    MaterialButton saveBudget;
    RecyclerView viewBudgetRecycler;
    ImageView backIcon;
    FloatingActionButton add_budget_item;

    BudgetItemAdapter adapter;

    public static ArrayList<BudgetItem> budgetItems;

    int budgetID;

    public static Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        Intent intent = getIntent();
        String budgetName = intent.getStringExtra("budgetName");
        budgetID = intent.getIntExtra("budgetID", -1);

        actionTxt = findViewById(R.id.view_budget_action_txt);
        backIcon = findViewById(R.id.new_budget_back);
        costTxt = findViewById(R.id.view_budget_cost);
        remainderTxt = findViewById(R.id.view_budget_remainder);
        saveBudget = findViewById(R.id.save_budget_btn);
        viewBudgetRecycler = findViewById(R.id.view_budget_recycler);
        add_budget_item = findViewById(R.id.add_budget_item);

        actionTxt.setText(budgetName);
        budgetItems = new ArrayList<>();
        add_budget_item.setScaleType(ImageView.ScaleType.CENTER_CROP);

        adapter = new BudgetItemAdapter(budgetItems, getSupportFragmentManager());

        viewBudgetRecycler.setLayoutManager(new LinearLayoutManager(this));
        viewBudgetRecycler.setHasFixedSize(true);
        viewBudgetRecycler.setAdapter(adapter);

        saveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Toast.makeText(ViewBudget.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        add_budget_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetItem budgetItem = new BudgetItem();
                budgetItem.setItemCost(0);
                budgetItem.setItemName("New Item");
                budgetItem.setItemQty(0);
                budgetItem.setGroupID(budgetID);
                budgetItem.setTimeStamp(System.currentTimeMillis());

                Number currentIdNum = realm.where(BudgetItem.class).max("ID");
                int nextId;
                if (currentIdNum == null) {

                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                budgetItem.setID(nextId);
                if (!Prevalent.allBudgetItems.contains(budgetItem)) {
                    Prevalent.allBudgetItems.add(budgetItem);
                }
                if (!budgetItems.contains(budgetItem)) {
                    budgetItems.add(budgetItem);
                }
                adapter.notifyDataSetChanged();

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.insertOrUpdate(budgetItem);

                    }
                });

            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        budgetItems.clear();


        for (BudgetItem item : Prevalent.allBudgetItems) {
            try {
                if (item.getGroupID() == budgetID) {
                    if (!budgetItems.contains(item)) {
                        budgetItems.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        updatePrice();
    }

    private void updatePrice() {
        Double totalPrice = 0.0;
        for (BudgetItem item : budgetItems) {
            Double itemCost = item.getItemCost() * item.getItemQty();
            totalPrice = totalPrice + itemCost;
        }
        costTxt.setText("K" + String.valueOf(totalPrice));

        for (BudgetModel model : Prevalent.allBudgets) {
            if (model.getID() == budgetID) {
                int index = Prevalent.allBudgets.indexOf(model);
                Double remainder = 0.0;
                remainder = model.getBudget() - totalPrice;
                remainderTxt.setText("K" + String.valueOf(remainder));

                realm.beginTransaction();
                model.setRemainder(remainder);
                model.setCost(totalPrice);
                realm.insertOrUpdate(model);
                realm.commitTransaction();

                Prevalent.allBudgets.remove(index);
                Prevalent.allBudgets.add(index, model);
                BudgetFragment.budgetAdapter.notifyDataSetChanged();


                break;
            }
        }
    }

    @Override
    public void applyTexts(String budgetName, double amount, int qty, int id) {
        realm.beginTransaction();
        for (BudgetItem budgetItem : Prevalent.allBudgetItems) {
            if (budgetItem.getID() == id) {

                budgetItem.setItemCost(amount);
                budgetItem.setItemName(budgetName);
                budgetItem.setItemQty(qty);
                realm.insertOrUpdate(budgetItem);
            }
        }
        for (BudgetItem budgetItem : budgetItems) {
            if (budgetItem.getID() == id) {

                budgetItem.setItemCost(amount);
                budgetItem.setItemName(budgetName);
                budgetItem.setItemQty(qty);
            }
        }
        realm.commitTransaction();

        adapter.notifyDataSetChanged();
        BudgetItemAdapter.budgetItemDialog.dismiss();

        updatePrice();

    }
}