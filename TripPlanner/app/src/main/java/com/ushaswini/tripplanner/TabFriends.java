package com.ushaswini.tripplanner;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * TabFriends
 * 18/04/2017
 */

public class TabFriends extends Fragment {

    ListView lt_friends;
    ListView lt_add_friends;
    ProgressBar progressBar;

    User currentUser;

    AdapterFriends friendsAdapter;
    AdapterFriends addFriendsAdapter;

    ArrayList<User> friends;
    ArrayList<User> addFriends;
    ArrayList<User> discoverFriends;
    ArrayList<User> receivedRequestFriends;
    ArrayList<User> sentRequestFriends;

    ArrayList<String> friendUids;



    DatabaseReference friendsDatabaseReference;
    String uid = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_friends, container, false);
        uid = (String) getArguments().get("currentUser");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try{
            friends = new ArrayList<>();
            addFriends = new ArrayList<>();
            discoverFriends = new ArrayList<>();
            receivedRequestFriends = new ArrayList<>();
            sentRequestFriends = new ArrayList<>();
            friendUids = new ArrayList<>();

            lt_friends = (ListView) getView().findViewById(R.id.lt_your_friends);
            lt_add_friends = (ListView) getView().findViewById(R.id.lt_add_friends);
            progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

            progressBar.setVisibility(View.VISIBLE);

            friendsAdapter = new AdapterFriends(getContext(),R.layout.custom_friend_row,friends,false,false,true);
            lt_friends.setAdapter(friendsAdapter);
            friendsAdapter.setNotifyOnChange(true);

            addFriendsAdapter = new AdapterFriends(getContext(),R.layout.custom_friend_row,addFriends,false,false,true);
            lt_add_friends.setAdapter(addFriendsAdapter);
            addFriendsAdapter.setNotifyOnChange(true);

            friendsDatabaseReference = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e){
            Toast.makeText(getContext(), "Error occured.", Toast.LENGTH_SHORT).show();
        }



        friendsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    discoverFriends.clear();
                    addFriends.clear();
                    friends.clear();
                    receivedRequestFriends.clear();
                    sentRequestFriends.clear();
                    friendUids.clear();

                    String friendUid;
                    User friend;


                    if (dataSnapshot.child("users/" + uid + "/friendsUids").exists()) {
                        for (DataSnapshot data : dataSnapshot.child("users/" + uid + "/friendsUids").getChildren()) {
                            friendUid = (String) data.getValue();
                            friend = dataSnapshot.child("users").child(friendUid).getValue(User.class);
                            friend.setStatus(User.FRIEND_STATUS.FRIEND);
                            friends.add(friend);
                            friendUids.add(friendUid);
                        }
                    }

                    if (dataSnapshot.child("users/" + uid + "/sentFriendRequestUids").exists()) {

                        for (DataSnapshot data : dataSnapshot.child("users/" + uid + "/sentFriendRequestUids").getChildren()) {
                            friendUid = (String) data.getValue();
                            friend = dataSnapshot.child("users").child(friendUid).getValue(User.class);
                            friend.setStatus(User.FRIEND_STATUS.SENT);
                            sentRequestFriends.add(friend);
                            friendUids.add(friendUid);
                        }
                    }

                    if (dataSnapshot.child("users/" + uid + "/receivedFriendRequestUids").exists()) {

                        for (DataSnapshot data : dataSnapshot.child("users/" + uid + "/receivedFriendRequestUids").getChildren()) {
                            friendUid = (String) data.getValue();
                            friend = dataSnapshot.child("users").child(friendUid).getValue(User.class);
                            friend.setStatus(User.FRIEND_STATUS.RECEIVED);
                            receivedRequestFriends.add(friend);
                            friendUids.add(friendUid);
                        }
                    }

                    User user;

                    for (DataSnapshot data : dataSnapshot.child("users").getChildren()) {
                        user = data.getValue(User.class);

                        if (!friendUids.contains(user.getUid()) && !user.getUid().equals(uid)) {
                            user.setStatus(User.FRIEND_STATUS.UNCONNECTED);
                            discoverFriends.add(user);
                        }
                    }
                    separateFriends();
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error occured.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void separateFriends(){

        try{
            addFriends.addAll(receivedRequestFriends);
            addFriends.addAll(sentRequestFriends);
            addFriends.addAll(discoverFriends);
            addFriendsAdapter.notifyDataSetChanged();
            friendsAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occured.", Toast.LENGTH_SHORT).show();
        }


    }


}
