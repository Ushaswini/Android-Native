package com.ushaswini.tripplanner;

import android.content.Intent;
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
import java.util.Map;

public class JoinTripActivity extends AppCompatActivity implements AdapterFriends.IHandleConnect {

    ImageView imCoverPhoto;
    TextView tvTripDetails;
    ListView lvFriends;
    Button btnJoin;
    Button btnCancel;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    ArrayList<String> tripMembersUid;
    ArrayList<User> friends;

    AdapterFriends adapter;
    String currentUserId;
    User user;
    String tripId;
    TripDetails currentTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_trip);

        setTitle("Join Trip");

        imCoverPhoto = (ImageView) findViewById(R.id.im_cover_photo);
        tvTripDetails = (TextView) findViewById(R.id.tv_title);
        lvFriends = (ListView) findViewById(R.id.lt_friends);
        btnJoin = (Button) findViewById(R.id.btn_join);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        if(getIntent().getExtras().containsKey("trip_id")){
            tripId = getIntent().getExtras().getString("trip_id");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference( );
        firebaseAuth = FirebaseAuth.getInstance();

        friends = new ArrayList<>();
        tripMembersUid = new ArrayList<>();

        adapter = new AdapterFriends(this, R.layout.custom_friend_row,friends);
        adapter.setNotifyOnChange(true);
        lvFriends.setAdapter(adapter);

        btnCancel.setOnClickListener(cancel_click_listener);
        btnJoin.setOnClickListener(join_click_listener);



        currentUserId = firebaseAuth.getCurrentUser().getUid();


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
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
            startActivity(intent);


        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    tripMembersUid.add(uid);
                    /*User friend = data.getValue(User.class);
                    if(!friend.getUid().equals(currentUserId))
                        friends.add(friend);*/
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
                    Picasso.with(JoinTripActivity.this).load(currentTrip.getImageUrl()).into(imCoverPhoto);
                    tvTripDetails.setText(currentTrip.getTitle());


                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void addFriend(User friendUser) {

        user.addToSentFriendRequestUid(friendUser.getUid());
        friendUser.addToReceivedFriendRequestUid(user.getUid());

        Map<String, Object> postCurrentUser = user.toMap();
        Map<String, Object> postUser = friendUser.toMap();



        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
        childUpdates.put("/users/" + friendUser.getUid(),postUser);

        databaseReference.updateChildren(childUpdates);
    }

    @Override
    public void displayReceivedMessage(User friendUser) {
        user.addToSentFriendRequestUid(friendUser.getUid());
        friendUser.addToReceivedFriendRequestUid(user.getUid());

        Map<String, Object> postCurrentUser = user.toMap();
        Map<String, Object> postUser = friendUser.toMap();



        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
        childUpdates.put("/users/" + friendUser.getUid(),postUser);

        databaseReference.updateChildren(childUpdates);
    }

    @Override
    public void displaySentMessage() {
        Toast.makeText(this, "Friend Request sent already.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void removeFriend(User friendUser) {

        user.removeFriendUid(friendUser.getUid());
        friendUser.removeFriendUid(user.getUid());

        Map<String, Object> postCurrentUser = user.toMap();
        Map<String, Object> postUser = friendUser.toMap();



        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + user.getUid()  ,postCurrentUser);
        childUpdates.put("/users/" + friendUser.getUid(),postUser);

        databaseReference.updateChildren(childUpdates);
        Toast.makeText(this, "Removed from your friends.", Toast.LENGTH_SHORT).show();

    }
}
