package com.prox.sunrisetodolistapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.prox.sunrisetodolistapp.R;

public class TextSizeDialog extends AppCompatDialogFragment {

    RadioGroup group;

    private TextSizeDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.text_size_dialog, null);

        builder.setView(view);
        group = view.findViewById(R.id.textSizeRadioGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.text_small:
                        listener.applyTexts("Small");
                        break;
                    case R.id.text_medium:
                        listener.applyTexts("Medium");
                        break;
                    case R.id.text_large:
                        listener.applyTexts("Large");
                        break;

                }

            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TextSizeDialogListener) context;
        } catch (ClassCastException exception) {

            throw new ClassCastException(context.toString() +
                    " must implement NewBudgetDialogListener");
        }
    }

    public interface TextSizeDialogListener {
        void applyTexts(String textSize);
    }
}
