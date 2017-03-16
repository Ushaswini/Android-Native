package com.ushaswini.triviaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IShareDataFromAsync{

    static final String URL = "http://dev.theappsdr.com/apis/trivia_json/index.php";
    static final String QUESTION_KEY = "QUESTIONS";

    ImageView imageView;
    Button btn_exit;
    Button btn_start;
    ProgressBar progressBar;
    TextView tv_loading;

    ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_start = (Button) findViewById(R.id.btn_start);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_main);
        tv_loading = (TextView) findViewById(R.id.tv_loadingTrivia);


        questions = new ArrayList<Question>();

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_start.setEnabled(false);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TriviaActivity.class);
                intent.putExtra(QUESTION_KEY,questions);
                startActivity(intent);
            }
        });

        new GetQuestionsAsyncTask(this).execute(URL);

    }

    @Override
    public void postQuestions(ArrayList<Question> questionsReceived) {

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.trivia));

        questions = questionsReceived;
        btn_start.setEnabled(true);

        if(questions != null){
            for(int i=0;i< questions.size();i++){
                Log.d("Data",questions.get(i).getQuestionText());
            }
        }else{
            Log.d("Data","Null");
        }
    }

    @Override
    public void postBitmap(Bitmap bitmap,int id) {

    }

    @Override
    public void updateProgressBar(boolean isComplete) {
        if(isComplete){
            progressBar.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            tv_loading.setVisibility(View.VISIBLE);
        }
    }
}
