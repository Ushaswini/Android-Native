package com.ushaswini.chitchat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * ResponseJSONUtil
 * 30/03/2017
 */

public class ResponseJSONUtil {

    static class ResponseJSONParser {

        public static ResponseDecoded parseResponse(String input) throws JSONException {

            ResponseDecoded response = new ResponseDecoded();

            JSONObject root = new JSONObject(input);

            response.setStatus(root.getString("status"));
            response.setToken(root.getString("data"));
            response.setMessage(root.getString("message"));

            return response;
        }

        public static ResponseDecoded parseSubscribedChannels(String input) throws JSONException {

            ResponseDecoded  response = new ResponseDecoded();

            JSONObject root = new JSONObject(input);

            response.setStatus(root.getString("status"));
            response.setMessage(root.getString("message"));

            JSONArray array = root.getJSONArray("data");
            ArrayList<Channel> channels = new ArrayList<>();
            if(array != null){
                for(int i = 0;i< array.length();i++){
                    JSONObject obj = array.getJSONObject(i).getJSONObject("channel");
                    Channel channel = new Channel();
                    channel.setChannel_id(obj.getString("channel_id"));
                    channel.setChannel_name(obj.getString("channel_name"));
                    channel.setSubscribed(true);
                    channels.add(channel);
                }
            }

            response.setChannels(channels);
            return response;
        }

        public static ResponseDecoded parseChannelsList(String input) throws JSONException {

            ResponseDecoded  response = new ResponseDecoded();
            System.out.print(input);

            JSONObject root = new JSONObject( input);

            response.setStatus(root.getString("status"));
            response.setMessage(root.getString("message"));

            JSONArray arry = root.getJSONArray("data");
            ArrayList<Channel> channels = new ArrayList<>();
            if(arry != null){
                for(int i = 0;i< arry.length();i++){
                    JSONObject obj = arry.getJSONObject(i);
                    Channel channel = new Channel();
                    channel.setChannel_id(obj.getString("channel_id"));
                    channel.setChannel_name(obj.getString("channel_name"));
                    channels.add(channel);
                }
            }

            response.setChannels(channels);
            return response;
        }

        public static ArrayList<Message> parseMessages(String input) throws JSONException, ParseException {

            ArrayList<Message> messages = new ArrayList<>();
            JSONObject root = new JSONObject(input);

            JSONArray array = root.getJSONArray("data");
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                Message message = new Message();
                message.setMsgtext(object.getString("messages_text"));
                message.setFname(object.getJSONObject("user").getString("fname"));
                message.setLname(object.getJSONObject("user").getString("lname"));
                message.setMsgTime(object.getString("msg_time"));
                messages.add(message);
            }

            return messages;
        }

    }


}
