package com.ushaswini.tripplanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
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
import java.util.UUID;

public class ViewTripActivity extends AppCompatActivity implements AdapterChat.IShareData {

    ImageView imCoverPhoto;
    TextView tvTripDetails;
    ListView lvChats;
    EditText etMessage;
    ImageButton imImageSend;
    ImageButton imMessageSend;


    DatabaseReference databaseReference;
    StorageReference storageReference;

    final int ACTIVITY_SELECT_IMAGE = 1234;

    ArrayList<MessageDetails> messages;
    AdapterChat adapterChat;

    TripDetails currentTrip;

    String userUid;
    String tripId;
    boolean isMemberOf;
    String userDisplayName;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        setTitle("Chat Room");

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


        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("trips");
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDisplayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        adapterChat = new AdapterChat(this, R.layout.custom_image_message, messages);
        lvChats.setAdapter(adapterChat);
        adapterChat.setNotifyOnChange(true);
    }


    @Override
    protected void onStart() {
        super.onStart();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                ArrayList<MessageDetails> data = new ArrayList<MessageDetails>();


                MessageDetails message;
                for (DataSnapshot snapshot : dataSnapshot.child(tripId + "/messages").getChildren()) {
                    Log.d("data", snapshot.getValue(MessageDetails.class).toString());
                    message = snapshot.getValue(MessageDetails.class);
                    if(message.getUsersWhoDeletedThisMessage() != null){
                        if(!message.getUsersWhoDeletedThisMessage().contains(userUid)){
                            data.add(message);
                        }
                    }else{
                        data.add(message);
                    }
                }

                messages.addAll(data);
                adapterChat.notifyDataSetChanged();


                if (dataSnapshot.child(tripId).exists()) {
                    currentTrip = dataSnapshot.child(tripId).getValue(TripDetails.class);
                    Picasso.with(ViewTripActivity.this).load(currentTrip.getImageUrl()).into(imCoverPhoto);
                    tvTripDetails.setText(currentTrip.getTitle());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(userUid.equals(currentTrip.getOrganizer_id())){
            getMenuInflater().inflate(R.menu.menu_trip_organizer, menu);
            return true;
        }else{
            getMenuInflater().inflate(R.menu.menu_trip_non_organizer, menu);
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(userUid.equals(currentTrip.getOrganizer_id())){
            switch (item.getItemId()){
                case R.id.action_remove:{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("Do you want to delete this trip?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference.child(tripId).removeValue();
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
                case R.id.action_add_friends:{

                    Intent i = new Intent(ViewTripActivity.this, AddFriendToTripActivity.class);
                    i.putExtra("user_id", userUid);
                    i.putExtra("trip_id", tripId);
                    startActivity(i);
                    break;

                }
                case R.id.action_add_location:{
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

            }

        }else{
            switch (item.getItemId()){
                case R.id.action_add_friends:{

                    Intent i = new Intent(ViewTripActivity.this, AddFriendToTripActivity.class);
                    i.putExtra("user_id", userUid);
                    i.putExtra("trip_id", tripId);
                    startActivity(i);
                    break;

                }
                case R.id.action_add_location:{
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
            }
        }
        return true;
    }



    View.OnClickListener send_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String text = etMessage.getText().toString();
            Date date = Calendar.getInstance().getTime();
            String key = UUID.randomUUID().toString();

            MessageDetails messageDetails = new MessageDetails(text, userDisplayName, date, "", false, key);

            currentTrip.addMessage(messageDetails);

            Map<String, Object> postValues = currentTrip.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(tripId, postValues);
            databaseReference.updateChildren(childUpdates);

            etMessage.setText("");


        }
    };

    View.OnClickListener gallery_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTIVITY_SELECT_IMAGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        storeImage(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                currentTrip.addPlaceToTrip(place);

                Map<String, Object> postValues = currentTrip.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(tripId, postValues);
                databaseReference.updateChildren(childUpdates);

                Log.i("demo", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
               Log.d("demo", status.getStatus().toString());
                Log.i("demo", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
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
                // Uri url = taskSnapshot.getDownloadUrl();

                Date date = Calendar.getInstance().getTime();
                String key = UUID.randomUUID().toString();

                MessageDetails messageDetails = new MessageDetails("", userDisplayName, date, taskSnapshot.getDownloadUrl().toString(), true, key);

                currentTrip.addMessage(messageDetails);

                Map<String, Object> postValues = currentTrip.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put( tripId, postValues);
                databaseReference.updateChildren(childUpdates);
            }
        });
    }

    @Override
    public void postComment(final int position) {
        final EditText et_comment = new EditText(this);
        et_comment.setHint("Enter Comment");

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enter Comment")
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String commentKey = UUID.randomUUID().toString();
                        Comment comment = new Comment(userDisplayName, et_comment.getText().toString(), Calendar.getInstance().getTime(), commentKey);

                        currentTrip.getMessages().get(position).addComment(comment);

                        Map<String, Object> postValues = currentTrip.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(tripId, postValues);
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
    }

    @Override
    public void deleteMessage(int position) {

        currentTrip.getMessages().get(position).addToUserWhoDeletedThisMessage(userUid);


        Map<String, Object> postValues = currentTrip.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(tripId, postValues);
        databaseReference.updateChildren(childUpdates);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewTripActivity.this, TabbedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
    }
}
