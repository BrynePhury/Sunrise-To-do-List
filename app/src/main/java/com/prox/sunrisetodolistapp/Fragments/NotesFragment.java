package com.prox.sunrisetodolistapp.Fragments;

import android.content.Intent;
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
import com.prox.sunrisetodolistapp.Adapters.NoteAdapter;
import com.prox.sunrisetodolistapp.NoteEditor;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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

    public static RecyclerView pinnedRecycler;
    public static TextView pinnedLabel;
    public static View pinnedBarrier;
    public static NoteAdapter pinnedAdapter;
    TextView nothing_txt;
    ImageView nothing_img;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notes, container, false);

        pinnedAdapter = new NoteAdapter(Prevalent.pinnedNotes,NoteAdapter.ELSE);

        RecyclerView notes_recycler = view.findViewById(R.id.notes_recyclerView);
        pinnedLabel = view.findViewById(R.id.pinned_label);
        pinnedBarrier = view.findViewById(R.id.pinned_barrier);
        nothing_img = view.findViewById(R.id.nothing_img);
        nothing_txt = view.findViewById(R.id.nothing_txt);

        pinnedRecycler = view.findViewById(R.id.pinned_notes_recyclerView);
        TextView note_label = view.findViewById(R.id.note_label);
        ExtendedFloatingActionButton addNote0Fab = view.findViewById(R.id.add_note_fab);

        notes_recycler.setHasFixedSize(true);
        notes_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        notes_recycler.setAdapter(new NoteAdapter(Prevalent.allNotes,NoteAdapter.ELSE));

        pinnedRecycler.setHasFixedSize(true);
        pinnedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        pinnedRecycler.setAdapter(pinnedAdapter);

        if (Prevalent.allNotes.isEmpty()) {
            nothing_img.setVisibility(View.VISIBLE);
            nothing_txt.setVisibility(View.VISIBLE);
        }

        if (Prevalent.allNotes.isEmpty()) {
            note_label.setVisibility(View.GONE);
        }

        if (Prevalent.pinnedNotes.isEmpty()){
            pinnedBarrier.setVisibility(View.GONE);
            pinnedLabel.setVisibility(View.GONE);
            pinnedRecycler.setVisibility(View.GONE);
        } else {
            pinnedBarrier.setVisibility(View.VISIBLE);
            pinnedLabel.setVisibility(View.VISIBLE);
            pinnedRecycler.setVisibility(View.VISIBLE);
        }


        addNote0Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoteEditor.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public static void pinNote(){

        if (Prevalent.pinnedNotes.isEmpty()){
            pinnedBarrier.setVisibility(View.GONE);
            pinnedLabel.setVisibility(View.GONE);
            pinnedRecycler.setVisibility(View.GONE);
        } else {
            pinnedBarrier.setVisibility(View.VISIBLE);
            pinnedLabel.setVisibility(View.VISIBLE);
            pinnedRecycler.setVisibility(View.VISIBLE);
        }
        pinnedAdapter.notifyDataSetChanged();
    }
}