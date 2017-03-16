package com.ushaswini.notekeeper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ushas on 27/02/2017.
 */

public class Note {
    private long _id;
    private  String note,time_string;
    private int priority,status;
    private Date updated_time;


    public Note(String note, int priority, int status, Date update_time) {
        this.note = note;
        this.priority = priority;
        this.status = status;
        this.updated_time = update_time;
    }

    public Note(){

    }

    public String getTime_string() {
        return time_string;
    }

    public void setTime_string(String time_string) {
        this.time_string = time_string;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        SimpleDateFormat f = new SimpleDateFormat();
        this.time_string = f.format(updated_time);
        //Date date = f.parse(c.getString(4));
        this.updated_time = updated_time;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id=" + _id +
                ", note='" + note + '\'' +
                ", update_time='" + updated_time + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
