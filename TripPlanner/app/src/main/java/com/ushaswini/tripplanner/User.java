package com.ushaswini.tripplanner;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * User
 * 18/04/2017
 */

public class User implements Serializable {

    private String fName, lName, imageUrl,uid;
    private ArrayList<User> friends;
    private ArrayList<TripDetails> trips;



    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<TripDetails> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<TripDetails> trips) {
        this.trips = trips;
    }

    public User(String fName, String lName, String imageUrl, String uid) {
        this.fName = fName;
        this.lName = lName;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    public User() {
    }

    public Map<String,Object> toMap(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("fName",fName);
        result.put("lName",lName);
        result.put("imageUrl",imageUrl);
        result.put("uid",uid);
        result.put("friends",friends);
        result.put("trips",trips);

        return result;
    }
}
