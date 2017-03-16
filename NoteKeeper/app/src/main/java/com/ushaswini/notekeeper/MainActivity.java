package com.ushaswini.notekeeper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ocpsoft.pretty.time.PrettyTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText et_note_name;
    Spinner sp_priority;
    Button btn_add;
    ListView lt_notes;

    ArrayList<Note>noteArrayList;
    int priorityIndex;
    DatabaseManager dm;
    NotesArrayAdapter notesArrayAdapter;
    /*ArrayList<Note> pendingNotesArrayList;
    ArrayList<Note> completedNotesArraylist;
    ArrayList<Note> allNotesArrayList;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_note_name = (EditText) findViewById(R.id.et_input);
        sp_priority = (Spinner) findViewById(R.id.sp_priority);
        btn_add = (Button) findViewById(R.id.btn_add);
        lt_notes = (ListView) findViewById(R.id.listview_notes);

        dm = new DatabaseManager(this);

        try {
            noteArrayList = (ArrayList<Note>) dm.getAllNotes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*pendingNotesArrayList = new ArrayList<>();
        completedNotesArraylist = new ArrayList<>();
        allNotesArrayList = new ArrayList<>();*/

        notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,noteArrayList,dm);
        lt_notes.setAdapter(notesArrayAdapter);
        notesArrayAdapter.setNotifyOnChange(true);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_priority.setAdapter(adapter);


        sp_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorityIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String input = et_note_name.getText().toString();
                    if(priorityIndex ==0 || input.equals("")){
                        Toast.makeText(MainActivity.this,"Give Correct Inputs",Toast.LENGTH_SHORT).show();
                    }else{
                        Note note = new Note();

                        note.setPriority(priorityIndex);

                        note.setNote(input);

                        Date date = Calendar.getInstance().getTime();
                        note.setUpdated_time(date);

                        dm.saveNote(note);

                        Log.d("note",note.toString());

                        ArrayList<Note> newNotes = (ArrayList<Note>) dm.getAllNotes();
                        noteArrayList.clear();
                        noteArrayList.addAll(newNotes);

                        Log.d("notes list count",noteArrayList.toString());

                        notesArrayAdapter.notifyDataSetChanged();
                        //Reset values
                        et_note_name.setText("");
                        sp_priority.setSelection(0);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

        lt_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int mPosition = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Do you really want to delete the task?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = noteArrayList.get(mPosition);
                                dm.deleteNote(note);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.showAll:{
                ArrayList<Note> notes = new ArrayList<>();
                try {
                    notes = (ArrayList<Note>) dm.getAllNotes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList<Note> pendingNotes = new ArrayList<>();
                ArrayList<Note> completedNotes = new ArrayList<>();

                for (Note note:notes) {
                    if(note.getStatus() ==0){
                        pendingNotes.add(note);
                    }else{
                        completedNotes.add(note);
                    }
                }
                notes.clear();
                notes.addAll(pendingNotes);
                notes.addAll(completedNotes);

                noteArrayList.clear();
                noteArrayList.addAll(notes);
                Log.d("all notes",notes.toString());
                Log.d("notes array",noteArrayList.toString());
                /*notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,allNotesArrayList,dm);
                lt_notes.setAdapter(notesArrayAdapter);
                notesArrayAdapter.setNotifyOnChange(true);*/
                notesArrayAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.showCompleted:{
                ArrayList<Note>notes = new ArrayList<>();
                try {
                    notes = (ArrayList<Note>) dm.getAllNotes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList<Note> completedNotes = new ArrayList<>();
                for (Note note:notes) {
                    if(note.getStatus() ==1){
                        completedNotes.add(note);
                    }
                }

                noteArrayList.clear();
                noteArrayList.addAll(completedNotes);

                /*notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,completedNotesArraylist,dm);
                lt_notes.setAdapter(notesArrayAdapter);
                notesArrayAdapter.setNotifyOnChange(true);*/
                notesArrayAdapter.notifyDataSetChanged();
                return true;
            }

            case R.id.showPending:{
                ArrayList<Note>notes = new ArrayList<>();

                try {
                    notes = (ArrayList<Note>) dm.getAllNotes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList<Note> pendingNotes = new ArrayList<>();
                for (Note note : notes) {
                    if(note.getStatus() ==0){
                        pendingNotes.add(note);
                    }
                }
                noteArrayList.clear();
                noteArrayList.addAll(pendingNotes);

                /*notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,pendingNotesArrayList,dm);
                lt_notes.setAdapter(notesArrayAdapter);
                notesArrayAdapter.setNotifyOnChange(true);*/
                notesArrayAdapter.notifyDataSetChanged();
                return true;
            }

            case R.id.sortByTime:{
                ArrayList<Note>notes = new ArrayList<>();

                try {
                    notes = (ArrayList<Note>) dm.getAllNotes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Collections.sort(notes,INCREASING_TIME);

                noteArrayList.clear();
                noteArrayList.addAll(notes);
                /*notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,noteArrayList,dm);
                lt_notes.setAdapter(notesArrayAdapter);
                notesArrayAdapter.setNotifyOnChange(true);*/
                notesArrayAdapter.notifyDataSetChanged();
                return true;
            }

            case R.id.sortByPriority:{
                ArrayList<Note>notes = new ArrayList<>();

                try {
                    notes = (ArrayList<Note>) dm.getAllNotes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Collections.sort(notes,INCREASING_PRIORITY);

                noteArrayList.clear();
                noteArrayList.addAll(notes);
                /*notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,noteArrayList,dm);
                lt_notes.setAdapter(notesArrayAdapter);
                notesArrayAdapter.setNotifyOnChange(true);*/
                notesArrayAdapter.notifyDataSetChanged();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    Comparator<Note> INCREASING_TIME = new Comparator<Note>() {
        @Override
        public int compare(Note o1, Note o2) {
            Date price1 = o1.getUpdated_time();
            Date price2 = o2.getUpdated_time();
            Date date1 = Calendar.getInstance().getTime();


            long val1 = TimeUnit.MILLISECONDS.toMinutes(date1.getTime()) - TimeUnit.MILLISECONDS.toMinutes(price1.getTime());
            long val2 = TimeUnit.MILLISECONDS.toMinutes(date1.getTime()) - TimeUnit.MILLISECONDS.toMinutes(price2.getTime());


            if (val1 > val2) {
                return 1;
            } else if (val1 < val2) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    Comparator<Note> INCREASING_PRIORITY = new Comparator<Note>() {
        @Override
        public int compare(Note o1, Note o2) {
            int price1 = o1.getPriority();
            int pri2 = o2.getPriority();

            if(price1 > pri2){
                return 1;
            }else if(price1 < pri2){
                return -1;
            }else{
                return 0;
            }
        }
    };
}
