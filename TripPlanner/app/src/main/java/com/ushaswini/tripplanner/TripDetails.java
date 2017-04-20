package com.ushaswini.tripplanner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * TripDetails
 * 18/04/2017
 */

public class TripDetails {
    private String title, location, imageUrl, trip_id, description, organizer_id;

    ArrayList<String> friendsUids;

    public ArrayList<String> getFriendsUids() {
        return friendsUids;
    }

    public void setFriendsUids(ArrayList<String> friendsUids) {
        this.friendsUids = friendsUids;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    public void addFriendUid (String friendUid){
        if(friendsUids == null){
            friendsUids = new ArrayList<>();
        }
        friendsUids.add(friendUid);
    }

    public TripDetails() {
    }

    public TripDetails(String title, String location, String imageUrl, String trip_id, String description,String organizer_id) {
        this.title = title;
        this.location = location;
        this.imageUrl = imageUrl;
        this.trip_id = trip_id;
        this.description = description;
        this.organizer_id = organizer_id;
    }

    public Map<String,Object> toMap(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("title",title);
        result.put("location",location);
        result.put("imageUrl",imageUrl);
        result.put("trip_id",trip_id);
        result.put("description",location);
        result.put("organizer_id",organizer_id);
        result.put("friends",friendsUids);


        return result;
    }
}
