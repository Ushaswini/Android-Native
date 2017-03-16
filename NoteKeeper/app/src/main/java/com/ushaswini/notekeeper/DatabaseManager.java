package com.ushaswini.notekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.List;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * ${NAME}
 * 27/02/2017
 */

public class DatabaseManager {

    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private NoteDAO noteDAO;

    public DatabaseManager(Context context){
        this.mContext = context;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        noteDAO = new NoteDAO(db);
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public NoteDAO getNoteDAO(){
        return this.noteDAO;
    }

    public long saveNote(Note note){
        return this.noteDAO.save(note);
    }

    public boolean updateNote(Note note){
        return this.noteDAO.update(note);
    }

    public boolean deleteNote(Note note){
        return this.noteDAO.delete(note);
    }

    public Note getNote(long id) throws ParseException {
        return this.noteDAO.get(id);
    }

    public List<Note> getAllNotes() throws ParseException {
        return this.noteDAO.getAll();
    }

}
