package com.ushaswini.tripplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * TripsTab
 * 18/04/2017
 */

public class TripsTab extends Fragment {

    User currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_trips, container, false);
        currentUser = (User) getArguments().get("currentUser");

        return rootView;
    }
}
