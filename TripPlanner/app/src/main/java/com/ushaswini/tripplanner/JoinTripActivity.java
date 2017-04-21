package com.ushaswini.tripplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JoinTripActivity extends AppCompatActivity {

    ImageView imCoverPhoto;
    TextView tvTripDetails;
    ListView lvFriends;
    Button btnJoin;
    Button btnCancel;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    ArrayList<String> friendsUids;
    ArrayList<User> friends;

    AdapterFriends adapter;
    String currentUserId;
    String tripId;
    TripDetails currentTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_trip);

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
        friendsUids = new ArrayList<>();

        adapter = new AdapterFriends(this, R.layout.custom_friend_row,friends,true);
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



        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsUids.clear();
                for(DataSnapshot data : dataSnapshot.child("trips/" + tripId + "/friendsUids").getChildren()){
                    String uid = (String) data.getValue();
                    friendsUids.add(uid);
                    /*User friend = data.getValue(User.class);
                    if(!friend.getUid().equals(currentUserId))
                        friends.add(friend);*/
                }

                for(String id : friendsUids){
                    User friend = dataSnapshot.child("users").child(id).getValue(User.class);
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


}
