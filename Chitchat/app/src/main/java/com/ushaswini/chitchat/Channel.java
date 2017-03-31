package com.ushaswini.chitchat;


import java.io.Serializable;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Channel
 * 27/03/2017
 */

public class Channel implements Serializable{
    String channel_id,channel_name;

    boolean isSubscribed;



    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
