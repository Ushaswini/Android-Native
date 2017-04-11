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

public class LogInActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    Button btn_login;
    Button btn_signup;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mFirebaseAuth = FirebaseAuth.getInstance();

        setTitle("Chat Room");


        et_email = (EditText) findViewById(R.id.editText_email);
        et_password = (EditText) findViewById(R.id.editText_password);
        btn_login = (Button) findViewById(R.id.button_login);
        btn_signup = (Button) findViewById(R.id.button_signup);
        btn_login.setOnClickListener(login_click_listener);
        btn_signup.setOnClickListener(signUp_click_listner);



    }

    View.OnClickListener signUp_click_listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
            startActivity(intent);

        }
    };

    View.OnClickListener login_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            if(email.equals("") || password.equals("")){
                Toast.makeText(LogInActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
            }else{
                mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LogInActivity.this,"Log in not successful",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent i=new Intent(LogInActivity.this,ChatActivity.class);
                            startActivity(i);
                        }
                    }
                });
            }




        }
    };
}
