package com.ushaswini.tripplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class TabbedActivity extends AppCompatActivity implements SettingsTab.handleSaveChanges, TripsTab.TripListner{


    final int ACTIVITY_SELECT_IMAGE = 1234;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference postsDatabaseReference;
    StorageReference storageReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    User user;
    FirebaseUser currentUser;
    TabLayout tabLayout;
    Uri imageUri;
    String fName,lName;
    FriendsTab friendsTab;
    SettingsTab settingsTab;
    TripsTab tripsTab;
    NotificationsTab notificationsTab;
    ArrayList<TripDetails> trips;
    ArrayList<User> friends;
    String currentUserUid;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_friends);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        trips = new ArrayList<>();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentUser = firebaseAuth.getCurrentUser();

                currentUserUid = currentUser.getUid();

                String displayName = currentUser.getDisplayName();
                String[] names = displayName.split(",");
                String fName = names[0];
                String lName = names[1];
                String imageUrl = currentUser.getPhotoUrl().toString();
                String user_id = currentUser.getUid();
                user = new User(fName,lName,imageUrl,user_id);

            }
        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("users").child(currentUserUid).exists()){
                    user = dataSnapshot.child("users").child(currentUserUid).getValue(User.class);
                    Log.d("user",user.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    public void changeImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),ACTIVITY_SELECT_IMAGE);
    }

    @Override
    public void saveNameChanges(final String fName, final String lName) {
        this.fName = fName;
        this.lName = lName;
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(fName + ", " + lName)

                .build();
        currentUser.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    user.setfName(fName);
                    user.setlName(lName);

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/" + currentUserUid + "/fName" ,fName);
                    childUpdates.put("/users/" + currentUserUid + "/lName",lName);

                    databaseReference.updateChildren(childUpdates);

                    Toast.makeText(TabbedActivity.this, "User Details updated", Toast.LENGTH_SHORT).show();

                }
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

    void changeProfileImage(Bitmap bitmap){


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] dataArray = baos.toByteArray();

        StorageReference reference = storageReference.child("profile_images/" + currentUserUid + ".png");

        UploadTask uploadTask = reference.putBytes(dataArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                imageUri = taskSnapshot.getDownloadUrl();

                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(imageUri)
                        .build();

                currentUser.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            settingsTab.postCurrentUserImage(imageUri.toString());
                            user.setImageUrl(taskSnapshot.getDownloadUrl().toString());

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/users/" + currentUserUid + "imageUrl",imageUri);
                            databaseReference.updateChildren(childUpdates);

                            Toast.makeText(TabbedActivity.this, "Profile Image updated", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }

    @Override
    public void addTrip() {
         Intent intent = new Intent(TabbedActivity.this,AddTripActivity.class);
        startActivity(intent);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("currentUser",user);


            switch(position){
                case 0: friendsTab = new FriendsTab();
                    return friendsTab;

                case 1: {
                    tripsTab = new TripsTab();
                    tripsTab.setArguments(bundle);
                    return tripsTab;

                }
                case 2: notificationsTab  = new NotificationsTab();
                    return notificationsTab;
                case 3: {
                    settingsTab = new SettingsTab();
                    settingsTab.setArguments(bundle);
                    return settingsTab;
                }

                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
