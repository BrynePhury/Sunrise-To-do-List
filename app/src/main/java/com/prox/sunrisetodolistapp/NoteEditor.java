package com.prox.sunrisetodolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prox.sunrisetodolistapp.Models.NoteModel;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NoteEditor extends AppCompatActivity {

    ExtendedFloatingActionButton save_note_fab;
    TextInputEditText titleInput;
    TextInputEditText noteInput;

    NoteModel noteModel;

    Realm realm;

    boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        Intent intent = getIntent();

        isEdit = intent.getBooleanExtra("isEdit", false);
        int noteId = intent.getIntExtra("noteID", -1);

        titleInput = findViewById(R.id.note_title_et);
        noteInput = findViewById(R.id.note_body_et);
        save_note_fab = findViewById(R.id.save_note_fab);

        if (Prevalent.settingsModel.getNoteFontSize().equals("Medium")) {
            titleInput.setTextSize(19);
            noteInput.setTextSize(19);
        } else if (Prevalent.settingsModel.getNoteFontSize().equals("Large")) {
            titleInput.setTextSize(24);
            noteInput.setTextSize(24);
        }

        if (isEdit) {
            for (NoteModel model : Prevalent.allNotes) {
                if (model.getNoteID() == noteId) {
                    noteModel = model;
                }
            }
        } else {
            noteModel = new NoteModel();

            Number currentIdNum = realm.where(NoteModel.class).max("noteID");
            int nextId;
            if (currentIdNum == null) {

                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            noteModel.setNoteID(nextId);


        }

        try {
            titleInput.setText(noteModel.getTitle());
            noteInput.setText(noteModel.getNote());
        } catch (Exception e) {
            e.printStackTrace();
        }

        save_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();

            }
        });

    }

    private void saveNote() {


        String title = titleInput.getText().toString();
        String noteBody = noteInput.getText().toString();

        if (TextUtils.isEmpty(title)) {
            titleInput.setError("Required");
            titleInput.requestFocus();

        } else {
            realm.beginTransaction();

            noteModel.setNote(noteBody);
            noteModel.setTitle(title);
            noteModel.setTimeStamp(System.currentTimeMillis());

            realm.commitTransaction();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(noteModel);

                }
            });

            if (!isEdit) {
                Prevalent.allNotes.add(noteModel);
            }
            Toast.makeText(NoteEditor.this, "Saved", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(NoteEditor.this, Home.class);
            intent.putExtra("isNote", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }

    }
}