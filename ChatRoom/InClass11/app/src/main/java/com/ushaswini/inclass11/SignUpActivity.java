package com.ushaswini.inclass11;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    EditText et_fName;
    EditText et_lName;
    EditText et_email;
    EditText et_choosePassword;
    EditText et_repeatPassword;
    Button btn_signup;
    Button btn_cancel;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    String fName,lName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();

        setTitle("Sign Up");


        et_fName = (EditText) findViewById(R.id.editText_fName);
        et_lName = (EditText) findViewById(R.id.editText_lName);
        et_email = (EditText) findViewById(R.id.editText_email);
        et_choosePassword = (EditText) findViewById(R.id.editText_password);
        et_repeatPassword = (EditText) findViewById(R.id.editText_repeat);
        btn_signup = (Button) findViewById(R.id.button_signup);
        btn_cancel = (Button) findViewById(R.id.button_cancel);
        btn_signup.setOnClickListener(signUp_click_listner);
        btn_cancel.setOnClickListener(cancel_click_listener);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fName + " " + lName).build();
                    user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Demo","Upadted");
                            }
                        }
                    });
                }
            }
        };

    }

    View.OnClickListener signUp_click_listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String  email,choose_password, repeat_password;
            fName = et_fName.getText().toString();
            lName = et_lName.getText().toString();
            email = et_email.getText().toString();
            choose_password = et_choosePassword.getText().toString();
            repeat_password = et_repeatPassword.getText().toString();

            if(fName.equals("") || lName.equals("") || email.equals("") ||choose_password.equals("") || repeat_password.equals("")){
                Toast.makeText(SignUpActivity.this,"Enter all details",Toast.LENGTH_SHORT).show();
            }else{
                if(!choose_password.equals(repeat_password)){
                    Toast.makeText(SignUpActivity.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                }else {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,choose_password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignUpActivity.this,"Account successfully created",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                                startActivity(intent);

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
}
