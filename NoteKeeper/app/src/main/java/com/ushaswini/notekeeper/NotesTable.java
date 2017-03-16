package com.ushaswini.notekeeper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.StringBuilderPrinter;

/**
 * Created by ushas on 27/02/2017.
 */

public class NotesTable {

    static final String TABLE_NAME = "notes";

    static final String COLUMN_ID ="_id";
    static final String COLUMN_NOTE="note";
    static final String COLUMN_PRIORITY = "priority";
    static final String COLUMN_UPDATE_TIME = "update_time";
    static final String COLUMN_STATUS = "status";

    static public void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();


        sb.append("CREATE TABLE "+TABLE_NAME+" ( ");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_NOTE + " text not null, ");
        sb.append(COLUMN_PRIORITY + " integer, ");
        sb.append(COLUMN_UPDATE_TIME + " text, ");
        sb.append(COLUMN_STATUS + " integer );");

        Log.d("create table",sb.toString());
        try {
            db.execSQL(sb.toString());
        } catch (Exception oExcep) {
            Log.d("excep",oExcep.getMessage());
            oExcep.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db,int oldVersion,int newOldVersion){
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
        NotesTable.onCreate(db);
    }
}
