package com.ushaswini.tripplanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnCreateNewAccount;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    View.OnClickListener login_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
            } else {
                mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Log in not successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                            //TODO START TAB ACTIVITY
                            Intent intent = new Intent(LoginActivity.this, TabbedActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }


        }
    };
    View.OnClickListener signup_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log in");

        mFirebaseAuth = FirebaseAuth.getInstance();


        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCreateNewAccount = (Button) findViewById(R.id.btn_create_new_account);

        btnLogin.setOnClickListener(login_listener);
        btnCreateNewAccount.setOnClickListener(signup_listener);
    }

    public boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (null != ni & ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
