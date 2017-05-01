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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ViewTripActivity extends AppCompatActivity implements AdapterChat.IShareData,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        AdapterPlaces.IShareData{

    ImageView imCoverPhoto;
    TextView tvTripDetails;
    ListView lvChats;
    EditText etMessage;
    ImageButton imImageSend;
    ImageButton imMessageSend;


    DatabaseReference databaseReference;
    ValueEventListener databaseChangeEventListener;

    StorageReference storageReference;

    final int ACTIVITY_SELECT_IMAGE = 1234;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    ArrayList<MessageDetails> messages;
    AdapterChat adapterChat;

    TripDetails currentTrip;
    User currentUser;

    String userUid;
    String tripId;
    boolean isMemberOf;
    String userDisplayName;

    GoogleApiClient mGoogleApiClient;


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
        setContentView(R.layout.activity_view_trip);

        try {


            setTitle("Chat Room");

            if (!isConnectedOnline()) {
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                finish();
            }

            messages = new ArrayList<>();

            imCoverPhoto = (ImageView) findViewById(R.id.im_cover_photo);
            tvTripDetails = (TextView) findViewById(R.id.trip_details);
            lvChats = (ListView) findViewById(R.id.lv_chats);
            etMessage = (EditText) findViewById(R.id.et_message);
            imMessageSend = (ImageButton) findViewById(R.id.im_send);
            imImageSend = (ImageButton) findViewById(R.id.im_photo);

            imMessageSend.setOnClickListener(send_click_listener);
            imImageSend.setOnClickListener(gallery_click_listener);


            if (getIntent().getExtras().containsKey("trip_id")) {
                tripId = getIntent().getExtras().getString("trip_id");
            }

            if (getIntent().getExtras().containsKey("isMemberOf")) {
                isMemberOf = (boolean) getIntent().getExtras().get("isMemberOf");
            }

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0 /* clientId */, this)
                    .addConnectionCallbacks(this)
                    .addApi(Places.GEO_DATA_API)
                    .build();


            storageReference = FirebaseStorage.getInstance().getReference();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userDisplayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

            adapterChat = new AdapterChat(this, R.layout.custom_image_message, messages);
            lvChats.setAdapter(adapterChat);
            adapterChat.setNotifyOnChange(true);

        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getLocalizedMessage());

        }
        databaseChangeEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    currentUser = dataSnapshot.child("users").child(userUid).getValue(User.class);
                    messages.clear();
                    ArrayList<MessageDetails> data = new ArrayList<MessageDetails>();


                    MessageDetails message;
                    for (DataSnapshot snapshot : dataSnapshot.child("trips/" + tripId + "/messages").getChildren()) {
                        Log.d("data", snapshot.getValue(MessageDetails.class).toString());
                        message = snapshot.getValue(MessageDetails.class);
                        if (message.getUsersWhoDeletedThisMessage() != null) {
                            if (!message.getUsersWhoDeletedThisMessage().contains(userUid)) {
                                data.add(message);
                            }
                        } else {
                            data.add(message);
                        }
                    }

                    messages.addAll(data);
                    adapterChat.notifyDataSetChanged();


                    if (dataSnapshot.child("trips/" + tripId).exists()) {
                        currentTrip = dataSnapshot.child("trips/" + tripId).getValue(TripDetails.class);
                        Picasso.with(ViewTripActivity.this).
                                load(currentTrip.getImageUrl()).
                                placeholder(R.mipmap.ic_loading_placeholder).
                                into(imCoverPhoto);
                        tvTripDetails.setText(currentTrip.getTitle() +  "\n" + currentTrip.getLocation());
                    }
                }catch (Exception e){
                    Toast.makeText(ViewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                    Log.d("demo",e.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
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

        try{
            if(userUid.equals(currentTrip.getOrganizer_id())){
                getMenuInflater().inflate(R.menu.menu_trip_organizer, menu);
                return true;
            }else{
                getMenuInflater().inflate(R.menu.menu_trip_non_organizer, menu);
                return true;
            }
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getLocalizedMessage());

        }

        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {

            if (userUid.equals(currentTrip.getOrganizer_id())) {

                switch (item.getItemId()) {
                    case R.id.action_remove: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle("Do you want to delete this trip?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try{
                                            databaseReference.child("trips/" + tripId).removeValue();
                                            Toast.makeText(ViewTripActivity.this, "Successfully deleted trip.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }catch (Exception e){
                                            Toast.makeText(ViewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                        break;
                    }
                    case R.id.action_add_friends: {

                        Intent i = new Intent(ViewTripActivity.this, AddFriendToTripActivity.class);
                        i.putExtra("user_id", userUid);
                        i.putExtra("trip_id", tripId);
                        startActivity(i);
                        break;

                    }
                    case R.id.action_add_location: {
                        try {
                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            // TODO: Handle the error.
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // TODO: Handle the error.
                        }
                        break;
                    }
                    case R.id.action_edit_location: {
                        Intent i = new Intent(ViewTripActivity.this, EditPlacesActivity.class);
                        i.putExtra("trip_id", tripId);
                        startActivity(i);
                        break;
                    }
                    case R.id.action_view_map: {
                        Intent i = new Intent(ViewTripActivity.this, MapViewOfPlacesActivity.class);
                        i.putExtra("places", currentTrip.getPlaces());
                        startActivity(i);
                        break;
                    }

                }

            } else {
                switch (item.getItemId()) {
                    case R.id.action_add_friends: {

                        Intent i = new Intent(ViewTripActivity.this, AddFriendToTripActivity.class);
                        i.putExtra("user_id", userUid);
                        i.putExtra("trip_id", tripId);
                        startActivity(i);
                        break;

                    }
                    case R.id.action_add_location: {
                        try {
                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                            Log.d("test--0", "start intent");
                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            // TODO: Handle the error.
                            Log.d("test --1", e.getLocalizedMessage());
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // TODO: Handle the error.
                            Log.d("test --2", e.getLocalizedMessage());

                        }
                        break;
                    }
                    case R.id.action_edit_location: {
                        Intent i = new Intent(ViewTripActivity.this, EditPlacesActivity.class);
                        i.putExtra("trip_id", tripId);
                        startActivity(i);
                        break;
                    }
                    case R.id.action_view_map: {
                        Intent i = new Intent(ViewTripActivity.this, MapViewOfPlacesActivity.class);
                        i.putExtra("places", currentTrip.getPlaces());
                        startActivity(i);
                        break;
                    }
                    case R.id.action_exit_group:{
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle("Do you want to exit this trip?")
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try{

                                            currentTrip.removeMemberFromTrip(userUid);
                                            currentUser.removeTripId(tripId);

                                            Map<String, Object> postValues = currentTrip.toMap();
                                            Map<String, Object> postUser = currentUser.toMap();

                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("trips/" + tripId, postValues);
                                            childUpdates.put("users/" + userUid,postUser);

                                            databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    Toast.makeText(ViewTripActivity.this, "Successfully exited trip.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });

                                        }catch (Exception e){
                                            Toast.makeText(ViewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                        break;
                    }
                }
            }
            return true;
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getLocalizedMessage());

        }
        return false;
    }



    View.OnClickListener send_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {


                String text = etMessage.getText().toString();
                Date date = Calendar.getInstance().getTime();
                String key = UUID.randomUUID().toString();

                MessageDetails messageDetails = new MessageDetails(text, userDisplayName, date, "", false, key);

                currentTrip.addMessage(messageDetails);

                Map<String, Object> postValues = currentTrip.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("trips/" + tripId, postValues);
                databaseReference.updateChildren(childUpdates);

                etMessage.setText("");
            }catch (Exception e){
                Toast.makeText(ViewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                Log.d("demo",e.getLocalizedMessage());

            }

        }
    };

    View.OnClickListener gallery_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTIVITY_SELECT_IMAGE);
            }catch (Exception e){
                Toast.makeText(ViewTripActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                Log.d("demo",e.getLocalizedMessage());

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case ACTIVITY_SELECT_IMAGE:{

                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                            storeImage(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("demo",e.getLocalizedMessage());

                        }

                    }
                }
                break;
            }
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:{

                try{
                    if (resultCode == RESULT_OK) {

                        new AlertDialog.Builder(this)
                                .setTitle("Add Place")
                                .setMessage("Do you really want to add this place to trip")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        Place place = PlaceAutocomplete.getPlace(ViewTripActivity.this, data);

                                        PlaceDetails placeToAdd = new PlaceDetails();
                                        placeToAdd.setAddress(String.valueOf(place.getAddress()));
                                        placeToAdd.setName(String.valueOf(place.getName()));
                                        placeToAdd.setLat(place.getLatLng().latitude);
                                        placeToAdd.setLng(place.getLatLng().longitude);

                                        Log.d("demo",placeToAdd.toString());
                                        boolean isAdded = currentTrip.addPlaceToTrip(placeToAdd);

                                        if(isAdded){
                                            Map<String, Object> postValues = currentTrip.toMap();
                                            Log.d("demo",currentTrip.toString());
                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("trips/" + tripId, postValues);
                                            databaseReference.updateChildren(childUpdates);

                                            Toast.makeText(ViewTripActivity.this, "Successfully Added Place to trip.", Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(ViewTripActivity.this, "Place already exists in trip.", Toast.LENGTH_SHORT).show();
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
                    }}catch (Exception e){
                    Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
                    Log.d("demo",e.getLocalizedMessage());

                }
                break;
            }
        }


    }

    void storeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] dataArray = baos.toByteArray();

        StorageReference reference = storageReference.child("messages_media/" + tripId + "/" + UUID.randomUUID().toString() + ".png");
        UploadTask uploadTask = reference.putBytes(dataArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Date date = Calendar.getInstance().getTime();
                String key = UUID.randomUUID().toString();

                MessageDetails messageDetails = new MessageDetails("", userDisplayName, date, taskSnapshot.getDownloadUrl().toString(), true, key);

                currentTrip.addMessage(messageDetails);

                Map<String, Object> postValues = currentTrip.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put( "trips/" + tripId, postValues);
                databaseReference.updateChildren(childUpdates);
            }
        });
    }

    @Override
    public void postComment(final MessageDetails message) {

        try {


            final EditText et_comment = new EditText(this);
            et_comment.setHint("Enter Comment");

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Enter Comment")
                    .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String commentKey = UUID.randomUUID().toString();
                            Comment comment = new Comment(userDisplayName, et_comment.getText().toString(), Calendar.getInstance().getTime(), commentKey);

                            for (MessageDetails messageLoop : currentTrip.getMessages()) {
                                if (messageLoop.getId().equals(message.getId())) {
                                    messageLoop.addComment(comment);
                                }
                            }

                            Map<String, Object> postValues = currentTrip.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("trips/" + tripId, postValues);
                            databaseReference.updateChildren(childUpdates);


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setView(et_comment);
            builder.show();
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getLocalizedMessage());

        }
    }

    @Override
    public void deleteMessage(final MessageDetails message) {

        try {


            new AlertDialog.Builder(this)
                    .setTitle("Delete this message")
                    .setMessage("Do you really want to delete this message")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            boolean index = currentTrip.getMessages().remove(message);
                            for (MessageDetails messageLoop : currentTrip.getMessages()) {
                                if (messageLoop.getId().equals(message.getId())) {
                                    messageLoop.addToUserWhoDeletedThisMessage(userUid);
                                }
                            }

                            Map<String, Object> postValues = currentTrip.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("trips/" + tripId, postValues);
                            databaseReference.updateChildren(childUpdates);


                            Toast.makeText(ViewTripActivity.this, "Message Deleted", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .show();
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getLocalizedMessage());

        }

    }

    @Override
    public void onBackPressed() {
        try{
            Intent intent = new Intent(ViewTripActivity.this, TabbedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            Log.d("demo",e.getLocalizedMessage());

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void editPlace(int position) {

    }

    @Override
    public void deletePlace(final int position) {


    }
}
