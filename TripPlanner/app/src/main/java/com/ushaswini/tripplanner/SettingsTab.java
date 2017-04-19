package com.ushaswini.tripplanner;


import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * SettingsTab
 * 18/04/2017
 */

public class SettingsTab extends Fragment {

    EditText etFirstName;
    EditText etLastName;
    Button btnSave;
    ImageButton imChangeImage;
    User currentUser;
    //Bundle args;

    private handleSaveChanges mListener;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_settings, container, false);
        currentUser = (User) getArguments().get("currentUser");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnSave = (Button) getView().findViewById(R.id.btn_save);
        etFirstName = (EditText) getView().findViewById(R.id.et_first_name);
        etLastName = (EditText) getView().findViewById(R.id.et_last_name);
        imChangeImage = (ImageButton) getView().findViewById(R.id.im_change_image);


        etFirstName.setText(currentUser.getfName());
        etLastName.setText(currentUser.getlName());
        btnSave.setOnClickListener(save_click_listener);
        imChangeImage.setOnClickListener(change_image_listener);
        Picasso.with(getContext()).load(currentUser.getImageUrl()).into(imChangeImage);


    }

    View.OnClickListener save_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.saveNameChanges(etFirstName.getText().toString(),etLastName.getText().toString());
        }
    };

    View.OnClickListener change_image_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.changeImage();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof handleSaveChanges) {
            mListener = (handleSaveChanges) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void postCurrentUserImage(String url){
        Picasso.with(getContext()).load(url).into(imChangeImage);



    }

    interface handleSaveChanges{
        void changeImage();
        void saveNameChanges(String fName, String lName );
    }
}
