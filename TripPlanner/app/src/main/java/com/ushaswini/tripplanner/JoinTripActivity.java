package com.ushaswini.tripplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinTripActivity extends AppCompatActivity implements AdapterFriends.IHandleConnect,AdapterPlaces.IShareData {

    ImageView imCoverPhoto;
    TextView tvTripDetails;
    ListView lvFriends;
    ListView lvPlaces;
    Button btnJoin;
    Button btnCancel;

    DatabaseReference databaseReference;
    ValueEventListener databaseChangeEventListener;
    FirebaseAuth firebaseAuth;


    ArrayList<String> tripMembersUid;
    ArrayList<User> friends;

    ArrayList<PlaceDetails> places;
    HashMap<Integer,PlaceDetails> placesMap;


    AdapterFriends adapter;
    AdapterPlaces adapterPlaces;

    String currentUserId;
    User user;
    String tripId;
    TripDetails currentTrip;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_trip);

        setTitle("Join Trip");

        try{
            if(!isConnectedOnline()){
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                finish();
            }

            imCoverPhoto = (ImageView) findViewById(R.id.im_cover_photo);
            tvTripDetails = (TextView) findViewById(R.id.tv_title);
            lvFriends = (ListView) findViewById(R.id.lt_friends);
            lvPlaces = (ListView) findViewById(R.id.lt_places);
            btnJoin = (Button) findViewById(R.id.btn_join);
            btnCancel = (Button) findViewById(R.id.btn_cancel);

            if(getIntent().getExtras().containsKey("trip_id")){
                tripId = getIntent().getExtras().getString("trip_id");
            }

            databaseReference = FirebaseDatabase.getInstance().getReference( );
            firebaseAuth = FirebaseAuth.getInstance();

            friends = new ArrayList<>();
            tripMembersUid = new ArrayList<>();

            places = new ArrayList<>();
            placesMap = new HashMap<>();

            adapterPlaces = new AdapterPlaces(this,R.layout.custom_places_row,places,false);
            lvPlaces.setAdapter(adapterPlaces);
            adapterPlaces.setNotifyOnChange(true);

            adapter = new AdapterFriends(this, R.layout.custom_friend_row,friends,false,false,false);
            adapter.setNotifyOnChange(true);
            lvFriends.setAdapter(adapter);

            btnCancel.setOnClickListener(cancel_click_listener);
            btnJoin.setOnClickListener(join_click_listener);



            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getMessage());
        }



        databaseChangeEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    tripMembersUid.clear();
                    friends.clear();

                    User userCurrent = dataSnapshot.child("users").child(currentUserId).getValue(User.class);

                    if(currentUserId != null){
                        if(dataSnapshot.child("users").child(currentUserId).exists()){
                            user = dataSnapshot.child("users").child(currentUserId).getValue(User.class);
                            Log.d("user",user.toString());
                        }
                    }
                    for(DataSnapshot data : dataSnapshot.child("trips/" + tripId + "/friendsUids").getChildren()){
                        String uid = (String) data.getValue();
                        if(!uid.equals(currentUserId)){
                            tripMembersUid.add(uid);
                        }
                    }


                    for(String id : tripMembersUid){

                        User friend =  dataSnapshot.child("users").child(id).getValue(User.class);

                        if(userCurrent.getFriendsUids().contains(friend.getUid())){
                            friend.setStatus(User.FRIEND_STATUS.FRIEND);

                        }else if(userCurrent.getReceivedFriendRequestUids().contains(friend.getUid())){
                            friend.setStatus(User.FRIEND_STATUS.RECEIVED);

                        }else if (userCurrent.getSentFriendRequestUids().contains(friend.getUid())){
                            friend.setStatus(User.FRIEND_STATUS.SENT);
                        }else{
                            friend.setStatus(User.FRIEND_STATUS.UNCONNECTED);
                        }

                        friends.add(friend);
                    }

                    if(dataSnapshot.child("trips").child(tripId).exists()){
                        currentTrip = dataSnapshot.child("trips").child(tripId).getValue(TripDetails.class);
                        Picasso.with(JoinTripActivity.this).
                                load(currentTrip.getImageUrl()).
                                placeholder(R.mipmap.ic_loading_placeholder).
                                into(imCoverPhoto);
                        tvTripDetails.setText(currentTrip.getTitle() + "\n" + currentTrip.getLocation());


                    }

                    adapter.notifyDataSetChanged();

                    Log.d("demo",currentTrip.toString());

                    List<PlaceDetails> data = currentTrip.getPlaces();
                    if(data != null){
                        places.clear();
                        places.addAll(data);
                    }else{
                        places.clear();
                    }

                    for(int i = 0;i<places.size();i++){
                        placesMap.put(i, places.get(i));
                    }
                    Log.d("demo",places.toString());
                    adapterPlaces.notifyDataSetChanged();

                }catch (Exception e){
                    Toast.makeText(JoinTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


    }

    View.OnClickListener cancel_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener join_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try{
                currentTrip.addFriendUid(currentUserId);
                user.addTripUid(tripId);

                Map<String, Object> postCurrentTrip = currentTrip.toMap();
                Map<String, Object> postCurrentUser = user.toMap();



                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
                childUpdates.put("/trips/" + tripId,postCurrentTrip);

                databaseReference.updateChildren(childUpdates);

                Toast.makeText(JoinTripActivity.this, "Successfully joined the trip.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(JoinTripActivity.this,ViewTripActivity.class);
                intent.putExtra("trip_id",tripId);
                startActivity(intent);
            }catch (Exception e){
                Toast.makeText(JoinTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
            }




        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(databaseChangeEventListener);


    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(databaseChangeEventListener);
    }

    @Override
    public void addFriend(User friendUser) {

        try{
            user.addToSentFriendRequestUid(friendUser.getUid());
            friendUser.addToReceivedFriendRequestUid(user.getUid());

            Map<String, Object> postCurrentUser = user.toMap();
            Map<String, Object> postUser = friendUser.toMap();



            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
            childUpdates.put("/users/" + friendUser.getUid(),postUser);

            databaseReference.updateChildren(childUpdates);
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void displayReceivedMessage(User friendUser) {
        try{
            user.addToSentFriendRequestUid(friendUser.getUid());
            friendUser.addToReceivedFriendRequestUid(user.getUid());

            Map<String, Object> postCurrentUser = user.toMap();
            Map<String, Object> postUser = friendUser.toMap();



            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
            childUpdates.put("/users/" + friendUser.getUid(),postUser);

            databaseReference.updateChildren(childUpdates);
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void displaySentMessage() {
        Toast.makeText(this, "Friend Request sent already.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void removeFriend(final User friendUser) {

        try{
            new AlertDialog.Builder(this)
                    .setTitle("Remove Friend")
                    .setMessage("Do you really want to remove this friend")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            user.removeFriendUid(friendUser.getUid());
                            friendUser.removeFriendUid(user.getUid());

                            Map<String, Object> postCurrentUser = user.toMap();
                            Map<String, Object> postUser = friendUser.toMap();



                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
                            childUpdates.put("/users/" + friendUser.getUid(),postUser);

                            databaseReference.updateChildren(childUpdates);
                            Toast.makeText(JoinTripActivity.this, "Removed from your friends.", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .show();

        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectFriend(int position, View v) {

    }

    @Override
    public void removeFriendFromTrip(int position) {

    }

    @Override
    public void editPlace(int position) {

    }

    @Override
    public void deletePlace(int position) {

    }
}
