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

public class NewBudgetDialog extends AppCompatDialogFragment {

    public TextInputEditText budgetNameInput;
    public TextInputEditText budgetAmountInput;
    private Button saveButton;

    private NewBudgetDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_budget_dialog, null);

        builder.setView(view);
        budgetNameInput = view.findViewById(R.id.budget_name_input_et);
        budgetAmountInput = view.findViewById(R.id.budget_amount_input_et);

        saveButton = view.findViewById(R.id.save_budget_btn);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetName = budgetNameInput.getText().toString();
                String stringAmt = budgetAmountInput.getText().toString();
                double amount;
                if (TextUtils.isEmpty(stringAmt)) {
                    amount = Double.parseDouble("0");
                } else {

                    amount = Double.parseDouble(stringAmt);
                }
                listener.applyTexts(budgetName, amount);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NewBudgetDialogListener) context;
        } catch (ClassCastException exception) {

            throw new ClassCastException(context.toString() +
                    " must implement NewBudgetDialogListener");
        }
    }

    public interface NewBudgetDialogListener {
        void applyTexts(String budgetName, double amount);
    }
}
