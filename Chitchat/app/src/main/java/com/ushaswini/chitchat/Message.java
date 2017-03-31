package com.ushaswini.chitchat;


import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Message
 * 27/03/2017
 */

public class Message {

    String fname, lname, msgtext, msgTime;

    Long msgDate;



    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMsgtext() {
        return msgtext;
    }

    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public Long getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Long msgDate) {
        this.msgDate = msgDate;
    }

    public void setMsgTime(String msgTime) throws ParseException {
        this.msgTime = msgTime;
        Log.d("date",msgTime);
        this.msgDate = Long.parseLong(msgTime);
        /*String date = DateUtils.formatElapsedTime(Long.parseLong(msgTime));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        this.setMsgDate(dateFormat.parse(date));*/

    }
}
