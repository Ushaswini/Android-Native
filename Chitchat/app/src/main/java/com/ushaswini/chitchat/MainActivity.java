package com.ushaswini.chitchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    Button btn_login;
    Button btn_signup;

    EditText et_email_log;
    EditText et_password_log;
    EditText et_email_sign;
    EditText et_fname;
    EditText et_lname;
    EditText et_password_sign;


    final static String BASE_URL = "http://52.90.79.130:8080/Groups/";
    final static String LOGIN_URL = BASE_URL + "api/login";
    final static String SIGN_UP_URL = BASE_URL + "api/signUp";
    final static String GET_SUBSCRIPTION_URL = BASE_URL + "api/get/subscriptions";
    final static String CHANNELS_URL = BASE_URL +"api/get/channels";
    final static String SUBSCRIBE_TO_CHANNEL = BASE_URL + "api/subscribe/channel";
    final static String GET_MESSAGE_URL = BASE_URL + "api/get/messages?channel_id=";
    final static String POST_MESSAGE_URL = BASE_URL + "api/post/message";


    final static String EMAIL_KEY = "email";
    final static String PASSWORK_KEY = "password";
    final static String FNAME_KEY = "fname";
    final static String LNAME_KEY = "lname";

    final static String PREF_TAG = "current_session";

    final static String TOKEN_TAG = "tag";

    SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;
    Set<String> nullSet;
    Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();

        btn_login = (Button) findViewById(R.id.button_login);
        btn_signup  = (Button) findViewById(R.id.button_signup);

        et_email_log = (EditText) findViewById(R.id.editText_email);
        et_password_log = (EditText) findViewById(R.id.editText_password);
        et_fname = (EditText) findViewById(R.id.editText_firstname);
        et_lname = (EditText) findViewById(R.id.editText_lastname);
        et_email_sign = (EditText) findViewById(R.id.editText_signup_email);
        et_password_sign = (EditText) findViewById(R.id.editText_signup_password);

        myPrefs = getSharedPreferences("com.ushaswini.chitchat", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();
        //nullSet = new ArraySet<String>();
        //set = myPrefs.getStringSet(PREF_TAG,nullSet);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestBody formBody = new FormBody.Builder()
                        .add(EMAIL_KEY, et_email_log.getText().toString())
                        .add(PASSWORK_KEY, et_password_log.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(LOGIN_URL)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        decodeResponse(response);
                        /*final Response response1 = response;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                decodeResponse(response1);
                            }
                        });*/
                    }
                });
            }
        });


        btn_signup.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              RequestBody formBody = new FormBody.Builder()
                      .add(EMAIL_KEY, et_email_sign.getText().toString())
                      .add(PASSWORK_KEY, et_password_sign.getText().toString())
                      .add(FNAME_KEY, et_fname.getText().toString())
                      .add(LNAME_KEY, et_lname.getText().toString())
                      .build();

              Request request = new Request.Builder()
                      .url(SIGN_UP_URL)
                      .post(formBody)
                      .build();

              client.newCall(request).enqueue(new Callback() {

                  @Override public void onFailure(Call call, IOException e) {
                      e.printStackTrace();
                  }

                  @Override public void onResponse(Call call, Response response) throws IOException {
                      decodeResponse(response);

                     /* final Response response1 = response;
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {

                         }
                     });*/
                  }
              });
          }
        });

    }

    private void decodeResponse(okhttp3.Response response){

        try {
            if(response.isSuccessful()){
                final ResponseDecoded decodedResponse = ResponseJSONUtil.ResponseJSONParser.parseResponse(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(decodedResponse.getStatus().equals("0"))
                        {
                            Toast.makeText(MainActivity.this,"Log-in not successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Commit to preferences
                            //set.add(decodedResponse.getToken());
                            prefsEditor.putString(PREF_TAG,decodedResponse.getToken());
                            prefsEditor.commit();

                            Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this,UserActivity.class);
                            intent.putExtra(TOKEN_TAG,decodedResponse.getToken());
                            startActivity(intent);
                        }
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
