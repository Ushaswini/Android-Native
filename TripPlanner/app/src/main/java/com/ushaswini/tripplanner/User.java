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

    private String fName, lName, imageUrl,uid, imageUid;
    private ArrayList<String> friendsUids;
    private ArrayList<String> tripUids;

    public String getImageUid() {
        return imageUid;
    }

    public void setImageUid(String imageUid) {
        this.imageUid = imageUid;
    }

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

    public ArrayList<String> getFriendsUids() {
        return friendsUids;
    }

    public void setFriendsUids(ArrayList<String> friendsUids) {
        this.friendsUids = friendsUids;
    }

    public ArrayList<String> getTripUids() {
        return tripUids;
    }

    public void setTripUids(ArrayList<String> tripUids) {
        this.tripUids = tripUids;
    }

    public User(String fName, String lName, String imageUrl, String uid) {
        this.fName = fName;
        this.lName = lName;
        this.imageUrl = imageUrl;
        this.uid = uid;

    }

    public void addTripUid(String tripUid){
        if(tripUids == null){
            tripUids = new ArrayList<>();
        }
        tripUids.add(tripUid);
    }

    public void addFriendUid(String friendUid){
        if(friendsUids == null){
            friendsUids = new ArrayList<>();

        }
        friendsUids.add(friendUid);
    }

    public User() {
    }

    public Map<String,Object> toMap(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("fName",fName);
        result.put("lName",lName);
        result.put("imageUrl",imageUrl);
        result.put("uid",uid);
        result.put("friendsUids",friendsUids);
        result.put("tripUids",tripUids);
        result.put("imageUid",imageUid);

        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", uid='" + uid + '\'' +
                ", imageUid='" + imageUid + '\'' +
                ", friendsUids=" + friendsUids +
                ", tripUids=" + tripUids +
                '}';
    }
}
