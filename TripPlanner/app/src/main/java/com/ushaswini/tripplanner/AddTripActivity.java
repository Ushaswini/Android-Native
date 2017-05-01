package com.ushaswini.tripplanner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity implements ISharePlaces,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    final int ACTIVITY_SELECT_IMAGE = 1234;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    final int ACTIVITY_ADD_FRIENDS = 2;

    ImageButton imCoverPhoto;
    Button btnCreate;
    Button btnCancel;
    EditText etName;
    EditText etDescription;
    AutoCompleteTextView at_destination;

    TripDetails trip;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    ValueEventListener databaseChangeEventListener;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;

    User user;
    String uid;
    String imageUri;
    ArrayList<String> trips;

    ArrayList<PlaceDetails> placesToAdd;
    ArrayList<String> friendsToAdd;

    GoogleApiClient mGoogleApiClient;

    PlacesTask placesTask;



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

            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String location  = at_destination.getText().toString();

            trip = new TripDetails(name,location,imageUri,uid,description,currentUser.getUid());

            trip.addFriends(friendsToAdd);
            trip.addLocations(placesToAdd);

            trip.addFriendUid(currentUser.getUid());

            Log.d("demo",trip.toString());
            user.addTripUid(uid);

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
        }
    };
    View.OnClickListener cancelTripListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

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
        setContentView(R.layout.activity_add_event);

        setTitle("Add Trip");

        try{
            if(!isConnectedOnline()){
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                finish();
            }

            imCoverPhoto = (ImageButton)findViewById(R.id.im_coverphoto);
            btnCreate = (Button) findViewById(R.id.btn_create);
            btnCancel = (Button) findViewById(R.id.btn_cancel);
            etName = (EditText) findViewById(R.id.et_trip_name);
            etDescription = (EditText)findViewById(R.id.et_trip_details);
            at_destination = (AutoCompleteTextView) findViewById(R.id.at_destination);
            //etLocation = (EditText) findViewById(R.id.et_location);

            imCoverPhoto.setOnClickListener(coverPhotoChangeListener);
            btnCreate.setOnClickListener(createTripListener);
            btnCancel.setOnClickListener(cancelTripListener);

            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            currentUser = mFirebaseAuth.getCurrentUser();

            uid = UUID.randomUUID().toString();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0 /* clientId */, this)
                    .addConnectionCallbacks(this)
                    .addApi(Places.GEO_DATA_API)
                    .build();

            trips = new ArrayList<>();
            placesToAdd = new ArrayList<>();
            friendsToAdd = new ArrayList<>();

        }catch (Exception e){
            Toast.makeText(AddTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
        }


        databaseChangeEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("users").child(currentUser.getUid()).exists()){
                    try{
                        user = dataSnapshot.child("users").child(currentUser.getUid()).getValue(User.class);
                        Log.d("user in reading",user.toString());
                    }catch (Exception e){
                        Toast.makeText(AddTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        at_destination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                at_destination.showDropDown();
                return false;
            }
        });

        at_destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                placesTask = new PlacesTask(AddTripActivity.this);
                placesTask.execute(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try{
            switch (item.getItemId()){
                case R.id.action_add_friends:{
                    Intent i = new Intent(AddTripActivity.this, AddFriendToNewTripActivity.class);
                    i.putExtra("user_id", user.getUid());
                    i.putExtra("trip_id", uid);
                    startActivityForResult(i,ACTIVITY_ADD_FRIENDS);
                    break;
                }
                case R.id.action_add_location:{
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                        Log.d("test--0","start intent");
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                        Log.d("test --1" , e.getLocalizedMessage());
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                        Log.d("test --2" , e.getLocalizedMessage());

                    }
                    break;
                }


            }

        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }



        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try{
            switch (requestCode){
                case ACTIVITY_SELECT_IMAGE:{
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
                    break;
                }

                case PLACE_AUTOCOMPLETE_REQUEST_CODE:{
                    if (resultCode == RESULT_OK) {

                        new AlertDialog.Builder(this)
                                .setTitle("Add Place")
                                .setMessage("Do you really want to add this place to trip")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        Place place = PlaceAutocomplete.getPlace(AddTripActivity.this, data);

                                        PlaceDetails placeToAdd = new PlaceDetails();
                                        placeToAdd.setAddress(String.valueOf(place.getAddress()));
                                        placeToAdd.setName(String.valueOf(place.getName()));
                                        placeToAdd.setLat(place.getLatLng().latitude);
                                        placeToAdd.setLng(place.getLatLng().longitude);

                                        if(!placesToAdd.contains(placeToAdd)){
                                            placesToAdd.add(placeToAdd);

                                            Toast.makeText(AddTripActivity.this, "Successfully Added Place to trip", Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(AddTripActivity.this, "Place already exists in trip.", Toast.LENGTH_SHORT).show();
                                        }


                                        Log.i("demo", "Place: " + place.getName());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .show();


                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                        Toast.makeText(this, "Error occured. Try after sometime.", Toast.LENGTH_SHORT).show();
                        Status status = PlaceAutocomplete.getStatus(this, data);
                        // TODO: Handle the error.
                        Log.d("demo", status.getStatus().toString());
                        Log.i("demo", status.getStatusMessage());

                    } else if (resultCode == RESULT_CANCELED) {
                        // The user canceled the operation.
                    }
                    break;
                }
                case ACTIVITY_ADD_FRIENDS:{
                    if(resultCode == RESULT_OK){
                        ArrayList<String> friends = (ArrayList<String>) data.getSerializableExtra("friends_to_add");
                        friendsToAdd.addAll(friends);
                        Toast.makeText(this, "Successfully added friends to activity", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            }

        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddTripActivity.this, "Cover photo saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void postPlacesResult(List<HashMap<String, String>> result) {
        try{
            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            at_destination.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
