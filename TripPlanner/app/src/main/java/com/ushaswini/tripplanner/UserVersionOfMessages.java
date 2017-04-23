package com.ushaswini.tripplanner;


import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * UserVersionOfMessages
 * 22/04/2017
 */

public class UserVersionOfMessages {

    ArrayList<String> messageUids;

    public ArrayList<String> getMessageUids() {
        return messageUids;
    }

    public void setMessageUids(ArrayList<String> messageUids) {
        this.messageUids = messageUids;
    }

    public void addToMessageListUids(String uid){
        if(messageUids == null){
            messageUids = new ArrayList<>();
        }
        messageUids.add(uid);
    }

    public void deleteFromMessageUids(String uid){
        if(messageUids != null){
            if(messageUids.size() > 0){
                messageUids.remove(uid);
            }
        }
    }


}
