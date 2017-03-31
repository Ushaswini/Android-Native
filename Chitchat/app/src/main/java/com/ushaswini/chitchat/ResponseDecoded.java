package com.ushaswini.chitchat;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * ResponseDecoded
 * 27/03/2017
 */

public class ResponseDecoded implements Serializable {

    String status, message,token;

    boolean isSubscribed;

    ArrayList<Channel> channels;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }
}


