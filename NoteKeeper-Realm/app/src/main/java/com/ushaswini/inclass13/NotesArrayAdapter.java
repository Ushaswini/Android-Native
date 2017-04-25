package com.ushaswini.inclass13;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ushas on 27/02/2017.
 */

public class NotesArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        TextView tv_note_name;
        TextView tv_priority;
        CheckBox cb_status;
        TextView tv_updated_time;

        public CustomViewHolder(View itemView) {
            super(itemView);
            tv_note_name = (TextView) itemView.findViewById(R.id.tv_note_name);
            tv_priority = (TextView) itemView.findViewById(R.id.tv_priority);
            cb_status = (CheckBox) itemView.findViewById(R.id.cb_status);
            tv_updated_time = (TextView) itemView.findViewById(R.id.tv_time);

            itemView.setLongClickable(true);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {

            final int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {


                Log.d("demo",position+"");
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Task")
                        .setMessage("Do you really want to delete the task?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                listener.deleteNote(notesList.get(position));
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
            return false;
        }


    }

    List<Note> notesList;

    int mResource;

    Context mContext;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_row_item,parent,false);

        holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CustomViewHolder viewHolder = (CustomViewHolder)holder;
        configureViewHolder(viewHolder,position);
    }

    public void configureViewHolder(CustomViewHolder holder, final int position) {

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

        //TODO Pretty time
        String tex = TimeUnit.MILLISECONDS.toMinutes(date1.getTime()) - TimeUnit.MILLISECONDS.toMinutes(date.getTime())  + " minutes ago";

        tv_updated_time.setText(tex);

        cb_status.setChecked(note.getStatus()>0);

        cb_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    final CheckBox cb = (CheckBox) buttonView;
                    if(!isChecked){
                        new AlertDialog.Builder(getContext())
                                .setTitle("Mark Pending")
                                .setMessage("Do you really want to mark it as pending?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        listener.changeNoteStatus(0,true,note);

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        cb.setChecked(!isChecked);

                                    }
                                })
                                .show();

                    }else{
                        listener.changeNoteStatus(1,false,note);

                        //dm.updateNote(note);

                    }
                    //dm.updateNote(notesList.get(position));
                }
            });

      /*  cb_status.setOnClickListener(new View.OnClickListener() {
            boolean isChecked = note.getStatus() > 0;
            @Override
            public void onClick(View v) {
                if(isChecked){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Mark Pending")
                            .setMessage("Do you really want to mark it as pending?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    listener.changeNoteStatus(0,true,note);



                                    //dm.updateNote(note);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    cb_status.setChecked(isChecked);

                                }
                            })
                            .show();

                }else{
                    listener.changeNoteStatus(1,false,note);
                    //note.setStatus(1);
                    //dm.updateNote(note);

                }
               // dm.updateNote(notesList.get(position));

            }
        });*/



    }


    @Override
    public int getItemCount() {
        if(notesList != null){
            return notesList.size();
        }else{
            return 0;
        }
    }

    public Context getContext() {
        return mContext;
    }

    handleNoteStatusChange listener;

    public NotesArrayAdapter(Context context, int resource, List<Note> notesList) {
        this.notesList =  notesList;
        this.mContext = context;
        this.mResource = resource;
        this.listener = (handleNoteStatusChange) context;
    }

    public interface handleNoteStatusChange{
        void changeNoteStatus(int status, boolean isChanged, Note note);
        void deleteNote(Note note);
    }





}
