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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.net.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    final int ACTIVITY_SELECT_IMAGE = 1234;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    EditText etFName;
    EditText etLName;
    EditText etEmail;
    EditText etChoosePassword;
    EditText etRepeatPassword;
    Button btnSignup;
    Button btnCancel;
    ImageButton btnChooseAvtar;

    String fName,lName, imageUid;
    Uri imageUri;
    Bitmap bitmap;


    View.OnClickListener signUp_click_listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String  email,choose_password, repeat_password;
            fName = etFName.getText().toString();
            lName = etLName.getText().toString();
            email = etEmail.getText().toString();
            choose_password = etChoosePassword.getText().toString();
            repeat_password = etRepeatPassword.getText().toString();

            if(fName.equals("") || lName.equals("") || email.equals("") ||choose_password.equals("") || repeat_password.equals("")){
                Toast.makeText(SignupActivity.this,"Enter all details",Toast.LENGTH_SHORT).show();
            }else{
                if(!choose_password.equals(repeat_password)){
                    Toast.makeText(SignupActivity.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                }else {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,choose_password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SignupActivity.this,"Account successfully created",Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        //TODO LOGIN ACTIVITY

                                    }
                                }
                            });
                }
            }


        }
    };
    View.OnClickListener cancel_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    View.OnClickListener choose_avtar_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),ACTIVITY_SELECT_IMAGE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        etFName = (EditText) findViewById(R.id.et_firstName);
        etLName = (EditText) findViewById(R.id.et_lastName);
        etEmail = (EditText) findViewById(R.id.et_email);
        etChoosePassword = (EditText) findViewById(R.id.et_password);
        etRepeatPassword = (EditText) findViewById(R.id.et_confirm_password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnChooseAvtar = (ImageButton) findViewById(R.id.im_choose_avtar);


        btnSignup.setOnClickListener(signUp_click_listner);
        btnCancel.setOnClickListener(cancel_click_listener);
        btnChooseAvtar.setOnClickListener(choose_avtar_click_listener);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        imageUid = UUID.randomUUID().toString();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {

                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fName + "," + lName)
                            .setPhotoUri(imageUri)
                            .build();


                    currentUser.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                String displayName = currentUser.getDisplayName();
                                String[] names = displayName.split(",");
                                String fName = names[0];
                                String lName = names[1];
                                String imageUrl =
                                currentUser.getPhotoUrl().toString();
                                String user_id =  currentUser.getUid();
                                User user = new User(fName,lName,imageUrl,user_id);
                                user.setImageUid(imageUid);

                                Map<String, Object> postValues = user.toMap();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/users/" + user_id,postValues);
                                databaseReference.updateChildren(childUpdates);
                            }
                        }
                    });



                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

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
    }
    void storeImage(Bitmap bitmap){


        //TODO Round button
        this.bitmap = bitmap;
        btnChooseAvtar.setImageBitmap(bitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] dataArray = baos.toByteArray();

        StorageReference reference = storageReference.child( "profile_images/" + imageUid + ".png");
        UploadTask uploadTask = reference.putBytes(dataArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageUri = taskSnapshot.getDownloadUrl();
            }
        });


    }

}
