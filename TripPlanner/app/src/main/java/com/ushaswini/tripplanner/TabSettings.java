package com.ushaswini.tripplanner;


import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * TabSettings
 * 18/04/2017
 */

public class TabSettings extends Fragment {

    EditText etFirstName;
    EditText etLastName;
    EditText etOldPassword;
    EditText etNewPassword;

    Button btnSave;
    ImageButton imChangeImage;
    User currentUser;
    RadioGroup radioGroup;
    RadioButton maleBtn;
    RadioButton femaleBtn;
    User.GENDER gender;
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
        etOldPassword = (EditText) getView().findViewById(R.id.et_password);
        etNewPassword = (EditText) getView().findViewById(R.id.et_newPassword);

        radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
        maleBtn = (RadioButton) getView().findViewById(R.id.male);
        femaleBtn = (RadioButton) getView().findViewById(R.id.female);



        etFirstName.setText(currentUser.getfName());
        etLastName.setText(currentUser.getlName());
        btnSave.setOnClickListener(save_click_listener);
        imChangeImage.setOnClickListener(change_image_listener);
        Picasso.with(getContext()).load(currentUser.getImageUrl()).into(imChangeImage);

        if(currentUser.getGender() == User.GENDER.FEMALE){
            femaleBtn.setChecked(true);
        }else{
            maleBtn.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.male: gender = User.GENDER.MALE;break;
                    case R.id.female: gender = User.GENDER.FEMALE; break;
                }
            }
        });


    }

    View.OnClickListener save_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String fName = etFirstName.getText().toString();
            String lName = etLastName.getText().toString();
            String oldPassword = etOldPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            mListener.saveChanges(fName,lName,oldPassword,newPassword,gender);
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
        void saveChanges(String fName, String lName , String oldPassword, String newPassword, User.GENDER gender );
    }
}
