package com.prox.sunrisetodolistapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.sunrisetodolistapp.Models.NoteModel;
import com.prox.sunrisetodolistapp.NoteEditor;
import com.prox.sunrisetodolistapp.Prevalent.Prevalent;
import com.prox.sunrisetodolistapp.R;
import com.prox.sunrisetodolistapp.Utility.HelperClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    ArrayList<NoteModel> noteModels;
    Context context;
    Realm realm;
    int callingAct;


    public static final int ARCHIVES = 100;
    public static final int ELSE = 200;

    public NoteAdapter(ArrayList<NoteModel> noteModels, int callingAct) {
        this.noteModels = noteModels;
        this.callingAct = callingAct;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_layout, parent, false);
        context = parent.getContext();
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteModel noteModel = noteModels.get(position);

        holder.noteTitle.setText(noteModel.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        Date lastEditDate = new Date(noteModel.getTimeStamp());
        holder.time.setText("Last Edit: " + sdf.format(lastEditDate));

        if (noteModel.getArchived() == NoteModel.UNARCHIVED) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callingAct == ELSE) {
                        Intent intent = new Intent(context, NoteEditor.class);
                        intent.putExtra("noteID", noteModel.getNoteID());
                        intent.putExtra("isEdit", true);
                        context.startActivity(intent);
                    }
                }
            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteModel model = realm.where(NoteModel.class)
                                .equalTo("noteID", noteModel.getNoteID()).findFirst();

                        realm.beginTransaction();
                        assert model != null;
                        model.deleteFromRealm();
                        realm.commitTransaction();

                        Prevalent.allNotes.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());

                        HelperClass.refreshTasks(context);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                CharSequence options[];
                if (noteModel.getArchived() == NoteModel.ARCHIVED) {
                    options = new CharSequence[]{
                            "UnArchive",
                            "Share",
                            "Delete"
                    };
                } else {
                    options = new CharSequence[]{
                            "Archive",
                            "Share",
                            "Delete"
                    };
                }


                dialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (noteModel.getArchived() == NoteModel.ARCHIVED) {
                                    realm.beginTransaction();
                                    noteModel.setArchived(NoteModel.UNARCHIVED);
                                    realm.commitTransaction();

                                    Prevalent.allNotes.add(noteModel);
                                    Prevalent.archivedNotes.remove(holder.getAdapterPosition());
                                } else {
                                    realm.beginTransaction();
                                    noteModel.setArchived(NoteModel.ARCHIVED);
                                    Prevalent.archivedNotes.add(noteModel);
                                    realm.insertOrUpdate(noteModel);
                                    realm.commitTransaction();

                                    Prevalent.allNotes.remove(holder.getAdapterPosition());
                                }
                                notifyItemRemoved(holder.getAdapterPosition());
                                Toast.makeText(context, "Archived", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                String title = noteModel.getTitle();
                                String body = "Title: " + noteModel.getTitle() + "\n\nBody: " +
                                        noteModel.getNote();
                                intent.putExtra(Intent.EXTRA_TEXT, title);
                                intent.putExtra(Intent.EXTRA_TEXT, body);
                                context.startActivity(Intent.createChooser(intent, "Share Note"));
                                break;
                            case 2:
                                builder.show();
                                break;
                        }

                    }
                });


                dialog.show();


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle;
        TextView time;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_item_title);
            time = itemView.findViewById(R.id.note_item_time);


        }
    }
}
