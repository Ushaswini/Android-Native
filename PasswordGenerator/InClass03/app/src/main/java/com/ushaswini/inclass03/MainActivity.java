package com.ushaswini.inclass03;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    ProgressDialog progressDialog;

    ArrayList<String> passwordsGeneratedThread;
    ArrayList<String>passwordsGeneratedAsync;

    ExecutorService threadPool;

    int progress;

    SeekBar sb_thread_count;
    SeekBar sb_thread_length;
    SeekBar sb_async_count;
    SeekBar sb_async_length;

    TextView tv_thread_count;
    TextView tv_thread_length;
    TextView tv_asyn_count;
    TextView tv_async_length;

    Button btn_generate;

    int thread_count =1;
    int thread_length = 7;
    int async_count = 1;
    int async_length =7;

    int total_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb_thread_count = (SeekBar)findViewById(R.id.seekBar_thread_count);
        sb_thread_length = (SeekBar)findViewById(R.id.seekBar_thread_length);
        sb_async_count = (SeekBar)findViewById(R.id.seekBar_async_count);
        sb_async_length = (SeekBar)findViewById(R.id.seekBar_async_length);

        tv_thread_count = (TextView)findViewById(R.id.tv_thread_count);
        tv_thread_length = (TextView)findViewById(R.id.tv_thread_length);
        tv_asyn_count = (TextView)findViewById(R.id.tv_async_count);
        tv_async_length = (TextView)findViewById(R.id.tv_async_length);

        btn_generate = (Button)findViewById(R.id.btn_generate);

        sb_thread_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thread_count = progress +1;
                tv_thread_count.setText(Integer.toString(thread_count));

                /*if(thread_count < 1){
                    seekBar.setProgress(1);
                }else{
                    tv_thread_count.setText(Integer.toString(thread_count));
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sb_thread_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thread_length = progress +7;
                tv_thread_length.setText(Integer.toString(thread_length));

                /*if(thread_length<7){
                    seekBar.setProgress(7);
                }else{
                   tv_thread_length.setText(Integer.toString(thread_length));
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sb_async_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                async_count = progress +1;
                tv_asyn_count.setText(Integer.toString(async_count));

                /*if(async_count<1){
                    seekBar.setProgress(1);
                }else{
                    tv_asyn_count.setText(Integer.toString(async_count));
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_async_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                async_length = progress +7;
                tv_async_length.setText(Integer.toString(async_length));


                /*if(async_length < 7){
                    seekBar.setProgress(7);
                }else{
                    tv_async_length.setText(Integer.toString(async_length));
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = 0;
                total_count = async_count +thread_count;

                passwordsGeneratedThread = new ArrayList<String>();
                passwordsGeneratedAsync = new ArrayList<String>();

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setProgress(0);
                progressDialog.setMax(total_count);
                progressDialog.setMessage("Generating Password");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                threadPool = Executors.newFixedThreadPool(2);
                threadPool.execute(new GetPasswordThread(thread_length,thread_count));
                new GetPasswordAsync().execute(async_length,async_count);
                progressDialog.show();
            }
        });



        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if(msg.getData().containsKey("PASSWORD")){
                    passwordsGeneratedThread.add(msg.getData().getString("PASSWORD"));
                    progress = progress +1;
                    progressDialog.setProgress(progress);
                    progressDialog.show();
                    if(progress == total_count){
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this,second_activity.class);
                        intent.putExtra("THREAD_DATA",passwordsGeneratedThread);
                        intent.putExtra("ASYNC_DATA",passwordsGeneratedAsync);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    class GetPasswordThread implements Runnable{

        int lengthOfPassword;
        int numberOfPasswords;

        GetPasswordThread (int lengthOfPassword, int numberOfPasswords){
            this.lengthOfPassword = lengthOfPassword;
            this.numberOfPasswords = numberOfPasswords;
        }
        @Override
        public void run() {

            for(int i=0;i<numberOfPasswords;i++){
                String passwordReceived = Util.getPassword(lengthOfPassword);
                Bundle bundle = new Bundle();
                bundle.putString("PASSWORD",passwordReceived);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }

    class GetPasswordAsync extends AsyncTask<Integer,String,String>{
        @Override
        protected String doInBackground(Integer... params) {

            for(int i=0;i<params[1];i++){
                String passwordReceived = Util.getPassword(params[0]);
                publishProgress(passwordReceived);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            passwordsGeneratedAsync.add(values[0]);
            progress = progress+1;
            progressDialog.setProgress(progress);
            progressDialog.show();
            if(progress == total_count){
                progressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this,second_activity.class);
                intent.putExtra("THREAD_DATA",passwordsGeneratedThread);
                intent.putExtra("ASYNC_DATA",passwordsGeneratedAsync);
                startActivity(intent);
            }
        }
    }
}
