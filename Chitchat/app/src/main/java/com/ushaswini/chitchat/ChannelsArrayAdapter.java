package com.ushaswini.chitchat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * ChannelsArrayAdapter
 * 30/03/2017
 */

public class ChannelsArrayAdapter extends ArrayAdapter<Channel> {

    ArrayList<Channel> channelList;
    int mResource;
    Context mContext;
    Activity activity;
    String token;

    public static final String CHANNEL_ID = "channel_id";


    public ChannelsArrayAdapter(Context context, int resource, ArrayList<Channel> channelList,String token) {
        super(context, resource);
        this.channelList = channelList;
        this.mContext = context;
        this.mResource = resource;
        this.activity = (Activity) context;
        this.token = token;
    }

    @Override
    public int getCount() {
        return channelList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder_Subscription holder;

        try {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource,parent,false);
                holder = new ViewHolder_Subscription();
                holder.button = (Button) convertView.findViewById(R.id.button_view);
                holder.textView = (TextView) convertView.findViewById(R.id.textView_channel_name);

                convertView.setTag(holder);
            }

            holder = (ViewHolder_Subscription) convertView.getTag();

            final Button btn = holder.button;
            TextView tv = holder.textView;


            final Channel channel = channelList.get(position);
            tv.setText(channel.channel_name);

            btn.setText(channel.isSubscribed ? "View" : "Join");

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(((Button)v).getText().equals("View"))
                    {
                        Intent intent = new Intent(mContext,MessageActivity.class);
                        intent.putExtra(CHANNEL_ID,channel);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        final OkHttpClient client = new OkHttpClient();

                        RequestBody formBody = new FormBody.Builder()
                                .add(CHANNEL_ID, channel.getChannel_id())
                                .build();

                        Request request = new Request.Builder()
                                .header("Authorization", "BEARER " + token)
                                .url(MainActivity.SUBSCRIBE_TO_CHANNEL)
                                .post(formBody)
                                .build();
                        //System.out.println(request.toString());
                        client.newCall(request).enqueue(new Callback() {

                            @Override public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {

                                System.out.println(response.isSuccessful());

                                try {
                                    ResponseDecoded decoded = ResponseJSONUtil.ResponseJSONParser.parseResponse(response.body().string());
                                    if(decoded.getStatus().equals("1")){
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                btn.setText("View");
                                                Toast.makeText(mContext,"Subscribed successfully",Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }

                }
            });



        }catch (Exception oExcep){
            Log.d("excep",oExcep.getMessage());
        }
        return convertView;
    }

}
