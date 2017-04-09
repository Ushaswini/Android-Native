package com.ushaswini.inclass03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class second_activity extends AppCompatActivity {


    ArrayList<String> passwords_thread;
    ArrayList<String>passwords_async;
    Button btn_finish;

    LinearLayout ln_thread;
    LinearLayout ln_async;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ln_thread = (LinearLayout)findViewById(R.id.linear_threads);
        ln_async = (LinearLayout)findViewById(R.id.linear_async);
        btn_finish = (Button)findViewById(R.id.finish);


        if(getIntent().getExtras() != null){
            passwords_thread = (ArrayList<String>) getIntent().getExtras().getSerializable("THREAD_DATA");
            passwords_async = (ArrayList<String>) getIntent().getExtras().getSerializable("ASYNC_DATA");

            if(passwords_thread != null){

                for (String str:passwords_thread ) {
                    Log.d("pthread",str);
                    TextView tv = new TextView(this);
                    tv.setText(str);
                    tv.setPadding(20,50,20,50);
                    tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                    ln_thread.addView(tv);
                }
            }
            if(passwords_async != null){

                for (String str:passwords_async ) {
                    Log.d("pasync",str);
                    TextView tv = new TextView(this);
                    tv.setText(str);
                    tv.setPadding(20,50,20,50);
                    tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                    ln_async.addView(tv);
                }
            }


        }

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
