package com.ushaswini.inclass11;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity  implements IShareData{

    TextView tv_title;
    EditText et_input;
    ImageButton im_send;
    ImageButton im_gallery;
    ImageButton im_logout;
    ListView listView;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseStorage storage;
    StorageReference storageReference;

    final int ACTIVITY_SELECT_IMAGE = 1234;

    ArrayList<MessageDetails> messages ;
    MessageAdapter messageAdapter;
    String user_name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        messages = new ArrayList<>();


        tv_title = (TextView) findViewById(R.id.tv_name);
        et_input = (EditText) findViewById(R.id.et_input);
        im_send = (ImageButton) findViewById(R.id.im_send);
        im_gallery = (ImageButton) findViewById(R.id.im_gallery);
        im_logout = (ImageButton) findViewById(R.id.im_logout);
        listView = (ListView) findViewById(R.id.listview);

        im_logout.setOnClickListener(logout_click_listener);
        im_gallery.setOnClickListener(gallery_click_listener);
        im_send.setOnClickListener(send_click_listener);

        user_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        tv_title.setText(user_name);

        messageAdapter = new MessageAdapter(this,R.layout.custom_image_message,messages);
        listView.setAdapter(messageAdapter);
        messageAdapter.setNotifyOnChange(true);

        setTitle("Chat");



    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/posts");
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                ArrayList<MessageDetails> data = new ArrayList<MessageDetails>();
                if(dataSnapshot.getChildrenCount() > 0){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Log.d("data",snapshot.getValue(MessageDetails.class).toString());
                        data.add(snapshot.getValue(MessageDetails.class));
                    }

                    messages.addAll(data);
                    messageAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    View.OnClickListener send_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = et_input.getText().toString();
            String title = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            Date date = Calendar.getInstance().getTime();
            String key = UUID.randomUUID().toString();

            MessageDetails messageDetails = new MessageDetails(text,title,date,"",false,key);
            Map<String, Object> postValues = messageDetails.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + key,postValues);
            databaseReference.updateChildren(childUpdates);


        }
    };

    View.OnClickListener gallery_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),ACTIVITY_SELECT_IMAGE);


            /*Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);*/
        }
    };

    View.OnClickListener logout_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(ChatActivity.this,LogInActivity.class);
            startActivity(i);
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

       /* switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    storeImage(yourSelectedImage);



            *//* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! *//*
                }*/


    }

     void storeImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] dataArray = baos.toByteArray();

        StorageReference reference = storageReference.child(UUID.randomUUID().toString() + ".png");
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

                String title = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Date date = Calendar.getInstance().getTime();
                String key = UUID.randomUUID().toString();

                MessageDetails messageDetails = new MessageDetails("",title,date, taskSnapshot.getDownloadUrl().toString(),true,key);
               // messageDetails.setImage_url(taskSnapshot.getDownloadUrl());
                Map<String, Object> postValues = messageDetails.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key,postValues);
                databaseReference.updateChildren(childUpdates);
            }
        });
    }

    @Override
    public void postComment(final MessageDetails messageDetails) {
        final EditText et_comment = new EditText(this);
        et_comment.setHint("Enter Comment");

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enter Comment")
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String commentKey=UUID.randomUUID().toString();
                        Comment comment = new Comment(user_name,et_comment.getText().toString(),Calendar.getInstance().getTime(),commentKey);

                        messageDetails.saddComments(comment);
                        databaseReference.child("posts").child(messageDetails.getId()).setValue(messageDetails);
                       /*
                        Map<String, Object> postValues = comment.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("/posts/" + messages.get(position).getId()+"/comments/"+commentKey ,postValues);
                        databaseReference.updateChildren(childUpdates);*/
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
    public void deleteImageMessage(MessageDetails messageDetails) {
        databaseReference.child("posts").child(messageDetails.getId()).removeValue();



    }


}
