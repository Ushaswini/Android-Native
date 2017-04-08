package com.ushaswini.chitchat;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * MessageAdapter
 * 30/03/2017
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    ArrayList<Message> messages;
    int mResource;
    Context mContext;
    Activity activity;
    String token;

    public MessageAdapter(Context context, int resource, ArrayList<Message> messages) {
        super(context, resource);
        this.messages = messages;
        this.mContext = context;
        this.mResource = resource;
        this.activity = (Activity) context;
        this.token = token;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_Message holder;

        try {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource,parent,false);
                holder = new ViewHolder_Message();
                holder.tv_message = (TextView) convertView.findViewById(R.id.textView_channel_body);

                convertView.setTag(holder);
            }

            holder = (ViewHolder_Message) convertView.getTag();
            TextView tv_message = holder.tv_message;
            final Message message = messages.get(position);

           // Date date = message.getMsgDate();
            Date date1 = Calendar.getInstance().getTime();
            //TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(message.getMsgTime()));

            /*SimpleDateFormat spm = new SimpleDateFormat();
            Date date = spm.parse(message.getMsgTime());

            PrettyTime time = new PrettyTime();
            String str = time.format(date);*/

            //Log.d("time",str);

            String time = TimeUnit.MILLISECONDS.toMinutes(date1.getTime()) - TimeUnit.MILLISECONDS.toMinutes(message.getMsgDate())  + " minutes ago";

            String text = message.getFname() + " " + message.getLname() + "\n" + message.getMsgtext() + "\n" + time;
            Log.d("text",text);
            tv_message.setText(text);



        }catch (Exception oExcep){
            Log.d("error",oExcep.getMessage());
        }

        return convertView;
    }
}
