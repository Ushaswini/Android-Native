package com.ushaswini.tripplanner;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class AddFriendToTripActivity extends AppCompatActivity implements AdapterFriends.IHandleConnect {

    User currentUser;
    TripDetails currentTrip;

    String tripId;
    String currentUserId;
    DatabaseReference databaseReference;

    ArrayList<String> friendUids;
    ArrayList<User> friends;
    ArrayList<String> friendsInCurrentTrips;
    ArrayList<Integer> selectedPositions;

    AdapterFriends adapter;
    ListView lt_friends;
    TextView tv_status;

    FloatingActionButton add;
    FloatingActionButton cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_to_trip);

        if (getIntent().getExtras().containsKey("trip_id")) {
            tripId = getIntent().getExtras().getString("trip_id");
        }

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friends = new ArrayList<>();
        friendUids = new ArrayList<>();
        friendsInCurrentTrips = new ArrayList<>();
        selectedPositions = new ArrayList<>();

        lt_friends = (ListView) findViewById(R.id.lt_friends);
        adapter = new AdapterFriends(this, R.layout.custom_friend_row, friends);
        lt_friends.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        tv_status = (TextView) findViewById(R.id.tv_status);

        add = (FloatingActionButton) findViewById(R.id.add);
        cancel = (FloatingActionButton) findViewById(R.id.cancel);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lt_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectedPositions.contains(position)) {
                    //remove
                    selectedPositions.remove(position);
                    view.setBackgroundColor(getResources().getColor(R.color.colorFriendMessage));
                } else {
                    selectedPositions.add(position);
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid;
                for (int position : selectedPositions) {
                    uid = friends.get(position).getUid();
                    currentTrip.addFriendUid(uid);
                }

                Map<String, Object> postTripValues = currentTrip.toMap();

                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("/trips/" + currentTrip.getTrip_id(), postTripValues);

                databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(AddFriendToTripActivity.this, "Friends added successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void addFriend(User user) {

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
        if (selectedPositions.contains(position)) {
            //remove
            selectedPositions.remove(position);
            v.setBackgroundColor(getResources().getColor(R.color.colorFriendMessage));
        } else {
            selectedPositions.add(position);
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }
}
