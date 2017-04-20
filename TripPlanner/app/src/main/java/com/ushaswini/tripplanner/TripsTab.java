package com.ushaswini.tripplanner;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.ushaswini.tripplanner.R.string.trips;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * TripsTab
 * 18/04/2017
 */

public class TripsTab extends Fragment {

    User currentUser;

    FloatingActionButton addTrip;
    ListView lv_your_trips;
    ListView lv_friend_trips;
    CustomTripAdapter your_adapter;

    private TripListner mListener;

    ArrayList<TripDetails> your_trips;
    DatabaseReference databaseReference;

    DatabaseReference yourTripsReference;
    DatabaseReference otherTripsReference;
    String uid = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_trips, container, false);
        currentUser = (User) getArguments().get("currentUser");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        your_trips = new ArrayList<>();

        addTrip = (FloatingActionButton) getView().findViewById(R.id.fab);
        lv_your_trips = (ListView) getView().findViewById(R.id.your_trips);
        lv_friend_trips = (ListView) getView().findViewById(R.id.friends_trips);


       // Log.d("demo",currentUser.getUid().toString());

        uid = currentUser.getUid().toString();

        your_adapter = new CustomTripAdapter(getContext(),R.layout.custom_trip_row,your_trips,false,uid);
        lv_your_trips.setAdapter(your_adapter);
        your_adapter.setNotifyOnChange(true);

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mListener.addTrip();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();


        yourTripsReference = FirebaseDatabase.getInstance().getReference("users/" + currentUser.getUid() + "/trips");

        yourTripsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("trips",dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                your_trips.clear();

                Log.d("demo",dataSnapshot.child("trips").toString());
                if(dataSnapshot.child("trips").getChildrenCount() > 0){

                    for(DataSnapshot data : dataSnapshot.child("trips").getChildren()){

                        TripDetails trip = data.getValue(TripDetails.class);
                        Log.d("demo",trip.toString());

                        your_trips.add(trip);
                    }

                    your_adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsTab.handleSaveChanges) {
            mListener = (TripListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void postYourTrips(ArrayList<TripDetails> trips){
        this.your_trips = trips;
        your_adapter.notifyDataSetChanged();
    }

    interface TripListner{
        void addTrip();
    }
}
