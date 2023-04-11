package com.prox.sunrisetodolistapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.prox.sunrisetodolistapp.Adapters.TaskAdapter;
import com.prox.sunrisetodolistapp.AddTask;
import com.prox.sunrisetodolistapp.Models.TaskModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ToDoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoListFragment newInstance(String param1, String param2) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static TaskAdapter overDueAdapter = new TaskAdapter(Prevalent.overDueTasks, TaskAdapter.OVERDUE_TASK);
    public static TaskAdapter todayAdapter = new TaskAdapter(Prevalent.todaySTasks, TaskAdapter.TODAY_TASK);
    public static TaskAdapter tomorrowAdapter = new TaskAdapter(Prevalent.tomorrowsTasks, TaskAdapter.TOMORROW_TASK);
    public static TaskAdapter laterAdapter = new TaskAdapter(Prevalent.laterDatesTasks, TaskAdapter.LATER_TASK);

    public static TaskAdapter todayFilteredAdapter;
    public static TaskAdapter tomorrowFilteredAdapter;
    public static TaskAdapter laterFilteredAdapter;


    public static ArrayList<TaskModel> filteredTodayTasks;
    public static ArrayList<TaskModel> filteredTomorrowTasks;
    public static ArrayList<TaskModel> filteredLaterTasks;

    public static TextView todayLabel;
    public static TextView tomorrowLabel;
    public static TextView laterLabel;
    public static TextView overdueLabel;

    TextView nothing_txt;
    ImageView nothing_img;

    public static RecyclerView todayRecyclerView;
    public static RecyclerView tomorrowRecyclerView;
    public static RecyclerView laterRecyclerView;
    public static RecyclerView overdueRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        filteredTodayTasks = new ArrayList<>();
        filteredTomorrowTasks = new ArrayList<>();
        filteredLaterTasks = new ArrayList<>();

        for (TaskModel model : Prevalent.todaySTasks) {
            if (!model.isCompleted() && !model.isDraft()) {
                filteredTodayTasks.add(model);
            }
        }
        for (TaskModel model : Prevalent.tomorrowsTasks) {
            if (!model.isCompleted() && !model.isDraft()) {
                filteredTomorrowTasks.add(model);
            }
        }
        for (TaskModel model : Prevalent.laterDatesTasks) {
            if (!model.isCompleted() && !model.isDraft()) {
                filteredLaterTasks.add(model);
            }
        }

        todayFilteredAdapter = new TaskAdapter(filteredTodayTasks, TaskAdapter.TODAY_TASK);
        tomorrowFilteredAdapter = new TaskAdapter(filteredTomorrowTasks, TaskAdapter.TOMORROW_TASK);
        laterFilteredAdapter = new TaskAdapter(filteredLaterTasks, TaskAdapter.LATER_TASK);

        //Setting up views
        todayLabel = view.findViewById(R.id.task_today_label);
        tomorrowLabel = view.findViewById(R.id.task_tomorrow_label);
        laterLabel = view.findViewById(R.id.task_later_label);
        overdueLabel = view.findViewById(R.id.task_overdue_label);
        todayRecyclerView = view.findViewById(R.id.todays_list_recycler);
        tomorrowRecyclerView = view.findViewById(R.id.tomorrows_list_recycler);
        laterRecyclerView = view.findViewById(R.id.later_list_recycler);
        overdueRecyclerView = view.findViewById(R.id.overdue_list_recycler);
        nothing_img = view.findViewById(R.id.nothing_img);
        nothing_txt = view.findViewById(R.id.nothing_txt);

        if (Prevalent.allTasks.isEmpty()) {
            nothing_img.setVisibility(View.VISIBLE);
            nothing_txt.setVisibility(View.VISIBLE);
        } else if (Prevalent.todaySTasks.isEmpty()
                && Prevalent.tomorrowsTasks.isEmpty()
                && Prevalent.laterDatesTasks.isEmpty()){
            nothing_img.setVisibility(View.VISIBLE);
            nothing_txt.setVisibility(View.VISIBLE);
        }

            //Checking which views to show
            if (Prevalent.overDueTasks.isEmpty()) {
                overdueLabel.setVisibility(View.GONE);
                overdueRecyclerView.setVisibility(View.GONE);
            }
        if (Prevalent.todaySTasks.isEmpty()) {
            todayLabel.setVisibility(View.GONE);
            todayRecyclerView.setVisibility(View.GONE);

        } else if (Prevalent.settingsModel.isDisappearingTasksEnabled()) {

            if (filteredTodayTasks.isEmpty()) {
                todayLabel.setVisibility(View.GONE);
                todayRecyclerView.setVisibility(View.GONE);
            }
        }
        if (Prevalent.tomorrowsTasks.isEmpty()) {
            tomorrowLabel.setVisibility(View.GONE);
            tomorrowRecyclerView.setVisibility(View.GONE);
        } else if (Prevalent.settingsModel.isDisappearingTasksEnabled()) {
            if (filteredTomorrowTasks.isEmpty()) {
                tomorrowLabel.setVisibility(View.GONE);
                tomorrowRecyclerView.setVisibility(View.GONE);
            }
        }
        if (Prevalent.laterDatesTasks.isEmpty()) {
            laterLabel.setVisibility(View.GONE);
            laterRecyclerView.setVisibility(View.GONE);
        } else if (Prevalent.settingsModel.isDisappearingTasksEnabled()) {
            if (filteredLaterTasks.isEmpty()) {
                laterLabel.setVisibility(View.GONE);
                laterRecyclerView.setVisibility(View.GONE);
            }
        }

        //Setting up Tasks recycler recyclers
        overdueRecyclerView.setHasFixedSize(true);
        overdueRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        todayRecyclerView.setHasFixedSize(true);
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tomorrowRecyclerView.setHasFixedSize(true);
        tomorrowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        laterRecyclerView.setHasFixedSize(true);
        laterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (Prevalent.settingsModel.isDisappearingTasksEnabled()) {
            todayRecyclerView.setAdapter(todayFilteredAdapter);
            tomorrowRecyclerView.setAdapter(tomorrowFilteredAdapter);
            laterRecyclerView.setAdapter(laterFilteredAdapter);
        } else {
            todayRecyclerView.setAdapter(todayAdapter);
            tomorrowRecyclerView.setAdapter(tomorrowAdapter);
            laterRecyclerView.setAdapter(laterAdapter);
        }

        overdueRecyclerView.setAdapter(overDueAdapter);

        //setting up add task button
        ExtendedFloatingActionButton mFab = view.findViewById(R.id.add_task_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start add task activity
                startActivity(new Intent(getContext(), AddTask.class));
            }
        });

        return view;
    }
}