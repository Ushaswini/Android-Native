package com.ushaswini.tripplanner;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

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

public class SearchResultsActivity extends AppCompatActivity implements AdapterFriends.IHandleConnect{

    String queryText = "";

    ListView lt_search_results;

    AdapterFriends adapter;

    ArrayList<User> searchResults;

    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    String currentUserUid;
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        handleIntent(getIntent());
        searchResults = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserUid = firebaseAuth.getCurrentUser().getUid();


        lt_search_results = (ListView) findViewById(R.id.lv_search_results);
        adapter = new AdapterFriends(this,R.layout.custom_friend_row,searchResults);
        lt_search_results.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.child(currentUserUid).getValue(User.class);
               /* Query  queryFName = databaseReference.orderByChild("fname").equalTo(queryText);
                Query queryLName = databaseReference.orderByChild("lname").equalTo(queryText);

                queryFName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                queryLName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User searchResult;
                        for(DataSnapshot  data : dataSnapshot.getChildren()){
                            searchResult = data.getValue(User.class);
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("fname").equalTo(queryText).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("lname").equalTo(queryText).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User searchResult;
                for(DataSnapshot  data : dataSnapshot.getChildren()){
                    searchResult = data.getValue(User.class);
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
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryText = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    public void addFriend(User user) {

    }

    @Override
    public void displayReceivedMessage(User friend) {

    }

    @Override
    public void displaySentMessage() {

    }

    @Override
    public void removeFriend(User user) {

    }

    @Override
    public void selectFriend(int position, View v) {

    }
}
