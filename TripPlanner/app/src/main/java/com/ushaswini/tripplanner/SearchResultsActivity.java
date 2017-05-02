package com.ushaswini.tripplanner;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity implements AdapterFriends.IHandleConnect{

    String queryText = "";

    ListView lt_search_results;

    AdapterFriends adapter;
    TextView tv_title;

    ArrayList<User> searchResults;

    DatabaseReference databaseReference;
    ValueEventListener databaseEventListener;
    ValueEventListener nameEventChangeListener;


    FirebaseAuth firebaseAuth;
    String currentUserUid;
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        try{
            tv_title = (TextView) findViewById(R.id.tv);


            handleIntent(getIntent());
            searchResults = new ArrayList<>();

            databaseReference = FirebaseDatabase.getInstance().getReference("users");
            firebaseAuth = FirebaseAuth.getInstance();
            currentUserUid = firebaseAuth.getCurrentUser().getUid();


            lt_search_results = (ListView) findViewById(R.id.lv_search_results);

            adapter = new AdapterFriends(this,R.layout.custom_friend_row,searchResults,false,false,true);
            lt_search_results.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }



        databaseEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.child(currentUserUid).getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        nameEventChangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    User searchResult;
                    for(DataSnapshot  data : dataSnapshot.getChildren()){
                        searchResult = data.getValue(User.class);

                        if(!searchResult.getUid().equals(currentUserUid)){
                            if(currentUser.getFriendsUids().contains(searchResult.getUid())){
                                searchResult.setStatus(User.FRIEND_STATUS.FRIEND);
                            }else if(currentUser.getSentFriendRequestUids().contains(searchResult.getUid())){
                                searchResult.setStatus(User.FRIEND_STATUS.SENT);
                            }else if(currentUser.getReceivedFriendRequestUids().contains(searchResult.getUid())){
                                searchResult.setStatus(User.FRIEND_STATUS.RECEIVED);
                            }else{
                                searchResult.setStatus(User.FRIEND_STATUS.UNCONNECTED);
                            }

                            searchResults.add(searchResult);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(SearchResultsActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
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
        databaseReference.addValueEventListener(databaseEventListener);
        databaseReference.orderByChild("lname").equalTo(queryText).addValueEventListener(nameEventChangeListener);
        databaseReference.orderByChild("fname").equalTo(queryText).addValueEventListener(nameEventChangeListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryText = intent.getStringExtra(SearchManager.QUERY);
            tv_title.setText("Search results for " + queryText);
            queryText = queryText.toLowerCase();
        }
    }

    @Override
    public void addFriend(User friendUser, final View v) {
        try{
            currentUser.addToSentFriendRequestUid(friendUser.getUid());
            friendUser.addToReceivedFriendRequestUid(currentUser.getUid());

            Map<String, Object> postCurrentUser = currentUser.toMap();
            Map<String, Object> postUser = friendUser.toMap();



            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + currentUser.getUid()  ,postCurrentUser);
            childUpdates.put("/users/" + friendUser.getUid(),postUser);

            databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    ((ImageButton)v).setImageResource(R.mipmap.ic_sent);

                }
            });

            Toast.makeText(this, "Friend request sent", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void displayReceivedMessage(User friend) {
        try{
            currentUser.addToSentFriendRequestUid(friend.getUid());
            friend.addToReceivedFriendRequestUid(currentUser.getUid());

            Map<String, Object> postCurrentUser = currentUser.toMap();
            Map<String, Object> postUser = friend.toMap();



            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + currentUser.getUid()  ,postCurrentUser);
            childUpdates.put("/users/" + friend.getUid(),postUser);

            databaseReference.updateChildren(childUpdates);

            Toast.makeText(this, "Friend request sent", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void displaySentMessage() {
        Toast.makeText(this, "Friend Request sent already.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void removeFriend(final User friendUser) {

        try{
            new AlertDialog.Builder(this)
                .setTitle("Remove Friend")
                .setMessage("Do you really want to remove this friend")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        currentUser.removeFriendUid(friendUser.getUid());
                        friendUser.removeFriendUid(currentUser.getUid());

                        Map<String, Object> postCurrentUser = currentUser.toMap();
                        Map<String, Object> postUser = friendUser.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/users/" + currentUser.getUid()  ,postCurrentUser);
                        childUpdates.put("/users/" + friendUser.getUid(),postUser);

                        databaseReference.updateChildren(childUpdates);

                        Toast.makeText(SearchResultsActivity.this, "Removed from friend list.", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .show();


        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectFriend(int position, View v) {

    }

    @Override
    public void removeFriendFromTrip(int position) {

    }
}