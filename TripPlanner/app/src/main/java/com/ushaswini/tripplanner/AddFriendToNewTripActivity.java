package com.ushaswini.tripplanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFriendToNewTripActivity extends AppCompatActivity implements AdapterFriends.IHandleConnect{

    User currentUser;
    TripDetails currentTrip;

    String tripId;
    String currentUserId;
    DatabaseReference databaseReference;
    ValueEventListener databaseChangeListener;

    ArrayList<String> friendUids;
    ArrayList<User> friends;
    ArrayList<String> friendsInCurrentTrips;
    ArrayList<Integer> selectedPositions;

    ArrayList<User> membersToAdd;

    AdapterFriends adapter;
    ListView lt_friends;
    TextView tv_status;

    ImageButton add;
    ImageButton cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_to_new_trip);

        try{
            if (getIntent().getExtras().containsKey("trip_id")) {
                tripId = getIntent().getExtras().getString("trip_id");
            }

            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            friends = new ArrayList<>();
            friendUids = new ArrayList<>();
            friendsInCurrentTrips = new ArrayList<>();
            selectedPositions = new ArrayList<>();

            membersToAdd = new ArrayList<>();

            lt_friends = (ListView) findViewById(R.id.lt_friends);
            adapter = new AdapterFriends(this, R.layout.custom_friend_row, friends,true,false,false);
            lt_friends.setAdapter(adapter);
            adapter.setNotifyOnChange(true);

            tv_status = (TextView) findViewById(R.id.tv_status);

            add = (ImageButton) findViewById(R.id.add);
            cancel = (ImageButton) findViewById(R.id.cancel);

            if(!isConnectedOnline()){
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                finish();
            }


            databaseReference = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e){
            Toast.makeText(this, "Exception occured.", Toast.LENGTH_SHORT).show();
        }



        databaseChangeListener = new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    friends.clear();
                    friendsInCurrentTrips.clear();
                    friendUids.clear();
                    selectedPositions.clear();

                    currentUser = dataSnapshot.child("users").child(currentUserId).getValue(User.class);
                    currentTrip = dataSnapshot.child("trips").child(tripId).getValue(TripDetails.class);

                    if (currentTrip == null) {
                        currentTrip = new TripDetails();
                    }
                    friendsInCurrentTrips = currentTrip.getFriendsUids();
                    friendUids = currentUser.getFriendsUids();

                    User friend;
                    if (friendUids != null) {
                        for (String friendUid : friendUids) {
                            if (friendsInCurrentTrips != null) {
                                if (!friendsInCurrentTrips.contains(friendUid)) {
                                    friend = dataSnapshot.child("users").child(friendUid).getValue(User.class);
                                    if (friend != null) {
                                        friend.setStatus(User.FRIEND_STATUS.FRIEND);
                                        friends.add(friend);
                                    }
                                }
                            } else {
                                friend = dataSnapshot.child("users").child(friendUid).getValue(User.class);
                                if (friend != null) {
                                    friend.setStatus(User.FRIEND_STATUS.FRIEND);
                                    friends.add(friend);
                                }
                            }

                        }
                    }

                    if(friends.size() == 0){
                        tv_status.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(AddFriendToNewTripActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        lt_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    if (selectedPositions.contains(position)) {
                        //remove
                        selectedPositions.remove(position);
                        view.setBackgroundColor(getResources().getColor(R.color.colorFriendMessage));
                    } else {
                        selectedPositions.add(position);
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                }catch(Exception e){
                    Toast.makeText(AddFriendToNewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    String uid;
                    for (int position : selectedPositions) {
                        uid = friends.get(position).getUid();
                        currentTrip.addFriendUid(uid);
                        membersToAdd.add(friends.get(position));
                    }

                    if(selectedPositions.size() > 0){
                        Intent intent = new Intent();
                        intent.putExtra("friends_to_add",currentTrip.getFriendsUids());
                        intent.putExtra("members",membersToAdd);
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                }catch(Exception e){
                    Toast.makeText(AddFriendToNewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (null != ni){
            if(ni.isConnected()){
                return true;
            }else{
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public void addFriend(User user,View v) {

    }

    @Override
    public void displayReceivedMessage(User friend) {

    }

    @Override
    public void displaySentMessage() {

    }

    @Override
    public void removeFriend(User user) {

    }

    @Override
    public void selectFriend(int position, View v) {

        try{
            if (selectedPositions.contains(position)) {
                //remove
                selectedPositions.remove(position);
                v.setBackgroundColor(getResources().getColor(R.color.colorFriendMessage));
            } else {
                selectedPositions.add(position);
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }catch(Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void removeFriendFromTrip(int position) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( databaseChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(databaseChangeListener);
    }
}
