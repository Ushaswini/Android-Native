package com.ushaswini.notekeeper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.id.list;

/**
 * Created by ushas on 27/02/2017.
 */

public class NoteDAO {

    private SQLiteDatabase db;

    public NoteDAO(SQLiteDatabase db){
        this.db = db;
    }

    public long save(Note note){

        ContentValues values = new ContentValues();

        values.put(NotesTable.COLUMN_NOTE,note.getNote());
        values.put(NotesTable.COLUMN_PRIORITY,note.getPriority());
        values.put(NotesTable.COLUMN_STATUS,note.getStatus());
        values.put(NotesTable.COLUMN_UPDATE_TIME,note.getTime_string());

        return db.insert(NotesTable.TABLE_NAME,null,values);
    }

    public boolean update(Note note){

        ContentValues values = new ContentValues();

        values.put(NotesTable.COLUMN_NOTE,note.getNote());
        values.put(NotesTable.COLUMN_PRIORITY,note.getPriority());
        values.put(NotesTable.COLUMN_STATUS,note.getStatus());
        values.put(NotesTable.COLUMN_UPDATE_TIME,note.getTime_string());

        return db.update(NotesTable.TABLE_NAME,values,NotesTable.COLUMN_ID+ " =?",new String[]{note.get_id()+""})>0;
    }

    public boolean delete(Note note){
        return db.delete(NotesTable.TABLE_NAME,NotesTable.COLUMN_ID +"=?",new String[]{note.get_id()+""})>0;
    }

    public Note get(long id) throws ParseException {
        Note note = null;
        Cursor cursor = db.query(true,NotesTable.TABLE_NAME,
                        new String[]{NotesTable.COLUMN_ID,NotesTable.COLUMN_NOTE,NotesTable.COLUMN_PRIORITY,
                                     NotesTable.COLUMN_STATUS,NotesTable.COLUMN_UPDATE_TIME},
                    NotesTable.COLUMN_ID+"=?",new String[]{id+""},null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            note = buildNoteFromCursor(cursor);
            if(!cursor.isClosed()){
                cursor.close();
            }
        }
        return note;
    }

    public List<Note> getAll() throws ParseException {

        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NotesTable.TABLE_NAME,new String[]{NotesTable.COLUMN_ID,NotesTable.COLUMN_NOTE,NotesTable.COLUMN_PRIORITY,
                                                        NotesTable.COLUMN_STATUS,NotesTable.COLUMN_UPDATE_TIME}
                                                        ,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                Note note = buildNoteFromCursor(cursor);
                if(note != null){
                    notes.add(note);
                }
            }while(cursor.moveToNext());

            if(cursor.isClosed()){
                cursor.close();
            }
        }
        return  notes;
    }

    private Note buildNoteFromCursor(Cursor c) throws ParseException {
        Note note = null;
        if(c != null){
            note = new Note();
            note.set_id(c.getLong(0));
            note.setNote(c.getString(1));
            note.setPriority(c.getInt(2));
            note.setStatus(c.getInt(3));
            SimpleDateFormat f = new SimpleDateFormat();
            Date date = f.parse(c.getString(4));
            note.setUpdated_time(date);
        }
        return  note;
    }
}
