package com.prox.sunrisetodolistapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NoteModel extends RealmObject {
    @PrimaryKey
    private int noteID;

    private String note;
    private String title;
    private long timeStamp;
    private int archived;
    private int pinned;

    final public static int PINNED = 1;
    final public static int UNPINNED = 0;
    final public static int ARCHIVED = 1;
    final public static int UNARCHIVED = 0;

    public NoteModel() {
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public int getPinned() {
        return pinned;
    }

    public void setPinned(int pinned) {
        this.pinned = pinned;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
