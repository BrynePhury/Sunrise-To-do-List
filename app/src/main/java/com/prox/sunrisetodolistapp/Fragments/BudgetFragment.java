package com.prox.sunrisetodolistapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.prox.sunrisetodolistapp.Adapters.BudgetAdapter;
import com.prox.sunrisetodolistapp.Dialogs.NewBudgetDialog;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BudgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
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

    public static NewBudgetDialog budgetDialog;

    public static BudgetAdapter budgetAdapter;
    public static TextView budget_label;
    TextView nothing_txt;
    ImageView nothing_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView budgets = view.findViewById(R.id.budget_recyclerView);
        budget_label = view.findViewById(R.id.budget_label);
        nothing_img = view.findViewById(R.id.nothing_img);
        nothing_txt = view.findViewById(R.id.nothing_txt);

        budgets.setHasFixedSize(true);
        budgets.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetAdapter = new BudgetAdapter(Prevalent.allBudgets,BudgetAdapter.ELSE);

        ExtendedFloatingActionButton newBudgetFab = view.findViewById(R.id.new_budget_fab);

        budgets.setAdapter(budgetAdapter);



        newBudgetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                budgetDialog = new NewBudgetDialog();
                budgetDialog.show(requireActivity().getSupportFragmentManager(), "New Budget Dialog");

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(Prevalent.allBudgets.isEmpty()){
            budget_label.setVisibility(View.GONE);
            nothing_img.setVisibility(View.VISIBLE);
            nothing_txt.setVisibility(View.VISIBLE);
        }
    }
}