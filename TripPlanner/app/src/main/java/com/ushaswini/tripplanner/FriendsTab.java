package com.ushaswini.tripplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * FriendsTab
 * 18/04/2017
 */

public class FriendsTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_friends, container, false);
        return rootView;
    }
}
