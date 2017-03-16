package com.ushaswini.notekeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ushas on 27/02/2017.
 */

public class NotesArrayAdapter extends ArrayAdapter<Note> {

    ArrayList<Note> notesList;
    int mResource;
    Context mContext;
    DatabaseManager dm;

    public NotesArrayAdapter(Context context, int resource, ArrayList<Note> notesList,DatabaseManager dm) {
        super(context, resource);
        this.notesList = notesList;
        this.mContext = context;
        this.mResource = resource;
        this.dm = dm;
    }

    @Override
    public int getCount() {
        if(notesList != null){
            return notesList.size();
        }else{
            return 0;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        try{
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource,parent,false);

                holder = new ViewHolder();
                holder.tv_note_name = (TextView) convertView.findViewById(R.id.tv_note_name);
                holder.tv_priority = (TextView) convertView.findViewById(R.id.tv_priority);
                holder.cb_status = (CheckBox) convertView.findViewById(R.id.cb_status);
                holder.tv_updated_time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();

            TextView tv_note_name = holder.tv_note_name;
            TextView tv_priority = holder.tv_priority;
            TextView tv_updated_time = holder.tv_updated_time;
            final CheckBox cb_status = holder.cb_status;

            final Note note = notesList.get(position);

            tv_note_name.setText(note.getNote());

            String priority="";

            switch (note.getPriority()){
                case 1: priority = "High priority";break;
                case 2: priority = "Medium priority"; break;
                case 3: priority = "Low priority"; break;
            }
            tv_priority.setText(priority);

            Date date = notesList.get(position).getUpdated_time();
            Date date1 = Calendar.getInstance().getTime();

            String tex = TimeUnit.MILLISECONDS.toMinutes(date1.getTime()) - TimeUnit.MILLISECONDS.toMinutes(date.getTime())  + " minutes ago";

            tv_updated_time.setText(tex);

            cb_status.setChecked(note.getStatus()>0);

            cb_status.setOnClickListener(new View.OnClickListener() {
                boolean isChecked = note.getStatus() > 0;
                @Override
                public void onClick(View v) {
                    if(isChecked){
                        new AlertDialog.Builder(getContext())
                                .setTitle("Mark Pending")
                                .setMessage("Do you really want to mark it as pending?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Date date = Calendar.getInstance().getTime();
                                        note.setUpdated_time(date);
                                        note.setStatus(0);
                                        dm.updateNote(note);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        cb_status.setChecked(isChecked);

                                    }
                                })
                                .show();

                    }else{
                        note.setStatus(1);
                        dm.updateNote(note);

                    }
                    dm.updateNote(notesList.get(position));

                }
            });


            /*cb_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    final CheckBox cb = (CheckBox) buttonView;
                    if(!isChecked){
                        new AlertDialog.Builder(getContext())
                                .setTitle("Mark Pending")
                                .setMessage("Do you really want to mark it as pending?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Date date = Calendar.getInstance().getTime();
                                        note.setUpdated_time(date);
                                        note.setStatus(0);
                                        dm.updateNote(note);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        cb.setChecked(!isChecked);

                                    }
                                })
                                .show();

                    }else{
                        note.setStatus(1);
                        dm.updateNote(note);

                    }
                    dm.updateNote(notesList.get(position));
                }
            });*/
        }catch (Exception oExcep){
            oExcep.printStackTrace();
        }

        return  convertView;

    }
}
