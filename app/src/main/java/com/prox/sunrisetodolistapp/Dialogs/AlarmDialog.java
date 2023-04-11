package com.prox.sunrisetodolistapp.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.R;

import java.util.Calendar;

public class AlarmDialog extends AppCompatDialogFragment {
    String taskTitle;
    int taskId;

    private AlarmDialogListener listener;

    public AlarmDialog(String taskTitle, int taskId) {
        this.taskTitle = taskTitle;
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_dialog, null);
        builder.setCancelable(false);

        builder.setView(view);

        TextView taskName_txt = view.findViewById(R.id.task_title);
        Button one_day_extend_btn = view.findViewById(R.id.one_day_extend_btn);
        Button one_week_extend_btn = view.findViewById(R.id.one_week_extend_btn);
        Button one_month_extend_btn = view.findViewById(R.id.one_month_extend_btn);

        taskName_txt.setText(taskTitle);

        one_day_extend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.extendTask(taskId,TaskModel.ONE_DAY_EXTENSION);

            }
        });
        one_week_extend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.extendTask(taskId,TaskModel.ONE_WEEK_EXTENSION);

            }
        });
        one_month_extend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.extendTask(taskId,TaskModel.ONE_MONTH_EXTENSION);

            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AlarmDialogListener) context;
        } catch (ClassCastException exception) {

            throw new ClassCastException(context.toString() +
                    " must implement AlarmDialogListener");
        }
    }

    public interface AlarmDialogListener {
        void extendTask(int id , int extension);
    }
}
