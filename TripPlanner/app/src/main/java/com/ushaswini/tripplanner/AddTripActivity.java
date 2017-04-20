package com.ushaswini.tripplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity {

    ImageButton imCoverPhoto;
    Button btnCreate;
    Button btnCancel;
    EditText etName;
    EditText etDescription;
    EditText etLocation;

    TripDetails trip;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;

    User user;

    String uid;
    String imageUri;



    final int ACTIVITY_SELECT_IMAGE = 1234;

    ArrayList<String> trips;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        Log.d("demo",intent.toString());



        imCoverPhoto = (ImageButton)findViewById(R.id.im_coverphoto);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        etName = (EditText) findViewById(R.id.et_trip_name);
        etDescription = (EditText)findViewById(R.id.et_trip_details);
        etLocation = (EditText) findViewById(R.id.et_location);

        imCoverPhoto.setOnClickListener(coverPhotoChangeListener);
        btnCreate.setOnClickListener(createTripListener);
        btnCancel.setOnClickListener(cancelTripListener);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();
        Log.d("id",currentUser.getUid());


        uid = UUID.randomUUID().toString();
        trips = new ArrayList<>();

    }

    @Override
    protected void onStart() {

        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo",dataSnapshot.child("trips").toString());
                if(dataSnapshot.child("trips").hasChild(uid)){

                }

                if(dataSnapshot.child("users").child(currentUser.getUid()).exists()){
                    user = dataSnapshot.child("users").child(currentUser.getUid()).getValue(User.class);
                    Log.d("user in reading",user.toString());
                }

                /*if(dataSnapshot.child("users").child(organizerId).child("trips").getChildrenCount() > 0){
                    for(DataSnapshot data : dataSnapshot.child("users").child(organizerId).child("trips").getChildren() ){
                        Log.d("demo",data.getValue().toString());
                        trips.add(data.getValue().toString());
                    }
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        changeProfileImage(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    void changeProfileImage(final Bitmap bitmap){



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] dataArray = baos.toByteArray();

        StorageReference reference = storageReference.child("profile_images/" + uid + ".png");

        UploadTask uploadTask = reference.putBytes(dataArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTripActivity.this, "Error occured. Cover Photo not saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //TODO Change image in image button
                imageUri = taskSnapshot.getDownloadUrl().toString();
                imCoverPhoto.setImageBitmap(bitmap);
                Log.d("image uri",imageUri);
                Toast.makeText(AddTripActivity.this, "Cover photo saved", Toast.LENGTH_SHORT).show();
            }
        });
    }


    View.OnClickListener coverPhotoChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),ACTIVITY_SELECT_IMAGE);
        }
    };

    View.OnClickListener createTripListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d("demo CLICK",currentUser.getUid());
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String location  = etLocation.getText().toString();

            trip = new TripDetails(name,location,imageUri,uid,description,currentUser.getUid());
            trip.addFriendUid(currentUser.getUid());
            user.addTripUid(uid);

            Log.d("user while writing",user.toString());



            Map<String, Object> postTripValues = trip.toMap();
            Map<String, Object> postUserValues = user.toMap();


            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/trips/" + uid ,postTripValues);
            childUpdates.put("/users/" + user.getUid(),postUserValues);

            databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError == null){
                        Toast.makeText(AddTripActivity.this, "Trip created successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            });

            /*Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("/users/" + currentUser.getUid(),postUserValues);
            databaseReference.updateChildren(userUpdates);*/

/*
            //trips.add(uid);

           // childUpdates.clear();
            Map<String, Object> userChildUpdates = new HashMap<>();

            userChildUpdates.put("/users/" + currentUser.getUid() + "/trips/"  + uid ,uid);
            databaseReference.updateChildren(userChildUpdates);

            //databaseReference.child("users").child(organizerId).child("trips").setValue(trips);*/


        }
    };

    View.OnClickListener cancelTripListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
