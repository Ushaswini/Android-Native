package com.ushaswini.notekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ushas on 27/02/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "mynotes.db";
    static final int DB_VERSION =1;

    SQLiteDatabase mDb;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        NotesTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        NotesTable.onUpgrade(db,oldVersion,newVersion);
    }
}
