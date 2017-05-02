package com.ushaswini.tripplanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnCreateNewAccount;


    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;

    SignInButton btnSignInButton;
    GoogleApiClient mGoogleApiClient;

    ArrayList<String> uids;




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

    View.OnClickListener google_signin_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent,1010);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log in");

        try{
            mFirebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            uids = new ArrayList<>();

            databaseReference.addValueEventListener(new ValueEventListener() {
                User user;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    uids.clear();

                    for(DataSnapshot data : dataSnapshot.child("users").getChildren()){
                        user = data.getValue(User.class);
                        uids.add(user.getUid());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            etEmail = (EditText) findViewById(R.id.et_email);
            etPassword = (EditText) findViewById(R.id.et_password);
            btnLogin = (Button) findViewById(R.id.btn_login);
            btnCreateNewAccount = (Button) findViewById(R.id.btn_create_new_account);
            btnSignInButton = (SignInButton) findViewById(R.id.btnSignIn);

            if(!isConnectedOnline()){
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                finish();
            }

            btnLogin.setOnClickListener(login_listener);
            btnCreateNewAccount.setOnClickListener(signup_listener);
            btnSignInButton.setOnClickListener(google_signin_listener);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("802803862492-m4mabg815h2c9b48d4r3sam11svrlcle.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode==1010){

                GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
        }


    }

    private void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){

            GoogleSignInAccount acc = result.getSignInAccount();

            firebaseAuthWithGoogle(acc);

            Log.d("Hello", acc.getDisplayName());
        }
        else {
        }

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        Log.d("demo", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Log in not successful", Toast.LENGTH_SHORT).show();
                        } else {

                            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();



                            if(currentUser != null){

                                if(currentUser.getDisplayName() != null){

                                    if(!uids.contains(currentUser.getUid())){

                                        if(currentUser.getPhotoUrl() != null){

                                            String imageUrl = currentUser.getPhotoUrl().toString();
                                            String user_id =  currentUser.getUid();
                                            String fName = acct.getGivenName();
                                            String lName = acct.getFamilyName();

                                            User user = new User(fName,lName,imageUrl,user_id);

                                            Map<String, Object> postValues = user.toMap();
                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("/users/" + user_id,postValues);
                                            databaseReference.updateChildren(childUpdates);
                                        }

                                    }


                                }
                            }
                            Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                            //TODO START TAB ACTIVITY
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            Intent intent = new Intent(LoginActivity.this, TabbedActivity.class);
                            startActivity(intent);
                        }

                        // ...
                    }
                });
    }


    public boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (null != ni){
            if(ni.isConnected()){
                return true;
            }else{
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
