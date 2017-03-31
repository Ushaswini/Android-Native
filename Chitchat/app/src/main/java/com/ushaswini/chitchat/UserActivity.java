package com.ushaswini.chitchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

import static com.ushaswini.chitchat.MainActivity.GET_SUBSCRIPTION_URL;

public class UserActivity extends AppCompatActivity{


    String token ="";

    ListView listView;
    Button btn_add;

    ArrayList<Channel> subscribedChannels;
    ArrayList<Channel> channelList;

    final static  String SUB_KEY = "sub";
    final static String CHA_KEY = "channels";
    final static String TOKEN_KEY = "token";

    OkHttpClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        listView = (ListView) findViewById(R.id.list_subscribed_channels_);
        btn_add = (Button) findViewById(R.id.button_addmore);


        client = new OkHttpClient();
        channelList = new ArrayList<>();
        subscribedChannels = new ArrayList<>();


        if(getIntent().getExtras().containsKey(MainActivity.TOKEN_TAG)){
            token = (String) getIntent().getExtras().get(MainActivity.TOKEN_TAG);
        }

        Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url(GET_SUBSCRIPTION_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                decodeSubListResponse(response);

                /*final Response response1 = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        decodeSubListResponse(response1);
                    }
                });*/
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btn_add.getText().equals("Done")){

                    Request request = new Request.Builder()
                            .header("Authorization", "BEARER " + token)
                            .url(GET_SUBSCRIPTION_URL)
                            .build();

                    client.newCall(request).enqueue(new Callback() {

                        @Override public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            decodeSubListResponse(response);

                           /* final Response response1 = response;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    decodeSubListResponse(response1);
                                }
                            });*/
                        }
                    });


                }else{
                    //if equals to Add more
                    Request request = new Request.Builder()
                            .header("Authorization", "BEARER " + token)
                            .url(MainActivity.CHANNELS_URL)
                            .build();

                    client.newCall(request).enqueue(new Callback() {

                        @Override public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            decodeChannelListResponse(response);

                            /*final Response response1 = response;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    decodeChannelListResponse(response1);
                                }
                            });*/
                        }
                    });
                }
            }
        });

    }

    private void decodeSubListResponse(Response response){

        try{
            if(response.isSuccessful()){
                final ResponseDecoded decodedResponse = ResponseJSONUtil.ResponseJSONParser.parseSubscribedChannels(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(decodedResponse.getStatus().equals("0")){
                            Toast.makeText(UserActivity.this,decodedResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }else{
                            btn_add.setText("Add more");
                            subscribedChannels = decodedResponse.getChannels();
                            ChannelsArrayAdapter arrayAdapter = new ChannelsArrayAdapter(UserActivity.this,R.layout.custom_row_subscription,subscribedChannels,token);
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void decodeChannelListResponse(Response response){
        try{
            if(response.isSuccessful()){
                final ResponseDecoded decodedResponse = ResponseJSONUtil.ResponseJSONParser.parseChannelsList(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(decodedResponse.getStatus().equals("0")){
                            Toast.makeText(UserActivity.this,decodedResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }else{
                            btn_add.setText("Done");
                            channelList = decodedResponse.getChannels();
                            for (Channel subChannel: subscribedChannels) {
                                for (Channel channel:channelList) {
                                    if(subChannel.getChannel_id().equals(channel.getChannel_id())){
                                        channel.setSubscribed(true);
                                    }
                                }
                            }

                            ChannelsArrayAdapter arrayAdapter = new ChannelsArrayAdapter(UserActivity.this,R.layout.custom_row_subscription,channelList,token);
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
