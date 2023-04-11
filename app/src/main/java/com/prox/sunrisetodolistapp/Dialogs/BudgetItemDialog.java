package com.prox.sunrisetodolistapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.prox.sunrisetodolistapp.R;

public class BudgetItemDialog extends AppCompatDialogFragment {

    public TextInputEditText budget_item_name_input_et;
    public TextInputEditText budget_item_price_input_et;
    public TextInputEditText budget_item_qty_input_et;
    private Button saveButton;

    private BudgetItemListener listener;
    int id;
    String itemName;
    Double itemPrice;
    int itemQty;

    public BudgetItemDialog(int id, String itemName, Double itemPrice,int itemQty) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.budget_item_dialog, null);

        builder.setView(view);
        budget_item_name_input_et = view.findViewById(R.id.budget_item_name_input_et);
        budget_item_price_input_et = view.findViewById(R.id.budget_item_price_input_et);
        budget_item_qty_input_et = view.findViewById(R.id.budget_item_qty_input_et);

        if (!itemName.equals("New Item")) {
            budget_item_name_input_et.setText(itemName);
        } else {
            budget_item_name_input_et.setText("");

        }
        if (itemPrice != 0) {
            budget_item_price_input_et.setText(String.valueOf(itemPrice));
        } else {
            budget_item_price_input_et.setText("");

        }
        if (itemQty != 0) {
            budget_item_qty_input_et.setText(String.valueOf(itemQty));
        } else {
            budget_item_qty_input_et.setText("");

        }
        saveButton = view.findViewById(R.id.save_budget_item_btn);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetItemName = budget_item_name_input_et.getText().toString();
                String budgetItemPrice = budget_item_price_input_et.getText().toString();
                String budgetItemQty = budget_item_qty_input_et.getText().toString();

                String name;
                if (TextUtils.isEmpty(budgetItemName)){
                    name = "New Item";
                } else {
                    name = budgetItemName;
                }
                int qty = 0;
                if (TextUtils.isEmpty(budgetItemQty)){
                    qty = 0;
                } else {
                    qty = Integer.parseInt(budgetItemQty);
                }
                double amount;
                if (TextUtils.isEmpty(budgetItemPrice)) {
                    amount = Double.parseDouble("0");
                } else {

                    amount = Double.parseDouble(budgetItemPrice);
                }
                listener.applyTexts(name, amount,qty,id);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (BudgetItemListener) context;
        } catch (ClassCastException exception) {

            throw new ClassCastException(context.toString() +
                    " must implement BudgetItemListener");
        }
    }

    public interface BudgetItemListener {
        void applyTexts(String budgetName, double amount,int qty, int id);
    }
}
