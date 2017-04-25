package com.ushaswini.inclass13;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements NotesArrayAdapter.handleNoteStatusChange {

    EditText et_note_name;
    Spinner sp_priority;
    Button btn_add;
    RecyclerView rv_notes;

    List<Note>noteArrayList;
    int priorityIndex;
    NotesArrayAdapter notesArrayAdapter;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_note_name = (EditText) findViewById(R.id.et_input);
        sp_priority = (Spinner) findViewById(R.id.sp_priority);
        btn_add = (Button) findViewById(R.id.btn_add);
        rv_notes = (RecyclerView) findViewById(R.id.rv_notes);

        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        noteArrayList = new ArrayList<>();



        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                noteArrayList = realm.where(Note.class).findAllSorted("status",Sort.ASCENDING);

                notesArrayAdapter.notifyDataSetChanged();
            }
        });

        noteArrayList = realm.where(Note.class).findAll();

        notesArrayAdapter = new NotesArrayAdapter(MainActivity.this,R.layout.list_row_item,noteArrayList);


        rv_notes.setAdapter(notesArrayAdapter);
        rv_notes.setLayoutManager(new LinearLayoutManager(MainActivity.this));



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

                    final String input = et_note_name.getText().toString();
                    if(priorityIndex ==0 || input.equals("")){
                        Toast.makeText(MainActivity.this,"Give Correct Inputs",Toast.LENGTH_SHORT).show();
                    }else{

                        final Note note = new Note();

                        note.setPriority(priorityIndex);

                        note.setNote(input);

                        Date date = Calendar.getInstance().getTime();
                        note.setUpdated_time(date);

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(note);
                            }
                        });


                        ArrayList<Note> newNotes = new ArrayList<Note>();

                        for(Note noteNew : realm.where(Note.class).findAll()){
                            newNotes.add(noteNew);
                        }

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

                noteArrayList = realm.where(Note.class).findAllSorted("status", Sort.ASCENDING);
                rv_notes.setAdapter(new NotesArrayAdapter(this,R.layout.list_row_item,noteArrayList));
                notesArrayAdapter.notifyDataSetChanged();

                Log.d("demo",noteArrayList.toString());


                return true;
            }
            case R.id.showCompleted:{

                noteArrayList = realm.where(Note.class).equalTo("status",1).findAll();
                rv_notes.setAdapter(new NotesArrayAdapter(this,R.layout.list_row_item,noteArrayList));

                notesArrayAdapter.notifyDataSetChanged();

                Log.d("demo",noteArrayList.toString());


                return true;
            }

            case R.id.showPending:{

                noteArrayList = realm.where(Note.class).equalTo("status",0).findAll();
                rv_notes.setAdapter(new NotesArrayAdapter(this,R.layout.list_row_item,noteArrayList));

                notesArrayAdapter.notifyDataSetChanged();

                Log.d("demo",noteArrayList.toString());


                return true;
            }

            case R.id.sortByTime:{
                noteArrayList = realm.where(Note.class).findAllSorted("updated_time",Sort.ASCENDING);
                rv_notes.setAdapter(new NotesArrayAdapter(this,R.layout.list_row_item,noteArrayList));

                notesArrayAdapter.notifyDataSetChanged();

                Log.d("demo",noteArrayList.toString());

                return true;
            }

            case R.id.sortByPriority:{

                noteArrayList = realm.where(Note.class).findAllSorted("priority",Sort.ASCENDING);
                rv_notes.setAdapter(new NotesArrayAdapter(this,R.layout.list_row_item,noteArrayList));

                notesArrayAdapter.notifyDataSetChanged();

                Log.d("demo",noteArrayList.toString());



                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public void changeNoteStatus(int status, boolean isChanged, Note note) {

        Note noteToUpdate = realm.where(Note.class).equalTo("_id", note.get_id()).findAll().first();

        if(isChanged){
            Date date = Calendar.getInstance().getTime();


            realm.beginTransaction();

            noteToUpdate.setStatus(status);
            noteToUpdate.setUpdated_time(date);

            realm.commitTransaction();

        }else{

            realm.beginTransaction();

            noteToUpdate.setStatus(status);

            realm.commitTransaction();

        }
    }

    @Override
    public void deleteNote(Note note) {
        final Note noteToDelete = realm.where(Note.class).equalTo("_id", note.get_id()).findAll().first();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                noteToDelete.deleteFromRealm();
            }
        });


    }
}
