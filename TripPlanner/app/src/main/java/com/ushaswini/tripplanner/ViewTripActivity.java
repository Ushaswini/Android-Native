package com.ushaswini.tripplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewTripActivity extends AppCompatActivity {

    ImageView imCoverPhoto;
    TextView tvTripDetails;
    ListView lvChats;
    EditText etMessage;
    ImageButton imImageSend;
    ImageButton imMessageSend;

    DatabaseReference databaseReference;

    String tripId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        imCoverPhoto = (ImageView) findViewById(R.id.im_cover_photo);
        tvTripDetails = (TextView) findViewById(R.id.trip_details);
        lvChats = (ListView) findViewById(R.id.lv_chats);
        etMessage = (EditText) findViewById(R.id.et_message);
        imMessageSend = (ImageButton) findViewById(R.id.im_send);
        imImageSend = (ImageButton) findViewById(R.id.im_photo);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        if(getIntent().getExtras().containsKey("trip_id")){
            tripId = getIntent().getExtras().getString("trip_id");


        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
