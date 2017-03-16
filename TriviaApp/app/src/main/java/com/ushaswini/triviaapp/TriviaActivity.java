package com.ushaswini.triviaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements IShareDataFromAsync{

    TextView tv_questionText;
    TextView tv_timeLeft;
    TextView tv_questionNum;
    LinearLayout linear_options;
    Button btn_previous;
    Button btn_next;
    ImageView imageView;
    RadioGroup radioGroup_options;
    ProgressBar progressBar;
    TextView tv_loading;

    ArrayList<Question> questions;
    int count;
    boolean isDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        setTitle("Trivia");

        tv_questionText = (TextView) findViewById(R.id.tv_questionText);
        tv_timeLeft = (TextView) findViewById(R.id.tv_timeRemaining);
        tv_questionNum = (TextView) findViewById(R.id.tv_questionNumber);
        linear_options = (LinearLayout) findViewById(R.id.linear_options);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        imageView = (ImageView) findViewById(R.id.imageView_ques);
        radioGroup_options = (RadioGroup) findViewById(R.id.radioGroup_options);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_trivia);
        tv_loading = (TextView) findViewById(R.id.tv_loadImage);

        btn_previous.setEnabled(false);

        if(getIntent().getExtras() != null){
            questions = getIntent().getExtras().getParcelableArrayList(MainActivity.QUESTION_KEY);

            if(questions != null){
                Question question_to_display = questions.get(0);
                createViewsAndSetParams(question_to_display);
            }
        }

        radioGroup_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int userAnswer = group.indexOfChild(group.findViewById(checkedId)) + 1;

                questions.get(count).setUserAnswerIndex(userAnswer);
                if(userAnswer == questions.get(count).getAnswerIndex()){
                    questions.get(count).setCorrectAnswer(true);
                }else{
                    questions.get(count).setCorrectAnswer(false);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_previous.setEnabled(true);
                if(count == questions.size()-1){
                    isDone = true;
                    Intent intent = new Intent(TriviaActivity.this,StatActivity.class);
                    intent.putExtra(MainActivity.QUESTION_KEY,questions);
                    startActivity(intent);
                }else{
                    if(count < questions.size()-1){
                        count = count+1;
                        createViewsAndSetParams(questions.get(count));
                    }
                    if(count == questions.size()-1){
                        btn_next.setText("Finish");
                    }
                }
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == questions.size()-1){
                    btn_next.setText("Next");
                }
                if(count > 0){
                    count = count-1;
                    Question question = questions.get(count);
                    createViewsAndSetParams(question);
                }
                if(count == 0){
                    btn_previous.setEnabled(false);
                }
            }
        });

        new CountDownTimer(120000,1000) {

            public void onTick(long millisUntilFinished) {
                tv_timeLeft.setText("Time Left: " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                if(!isDone){
                    isDone = true;
                    Intent intent = new Intent(TriviaActivity.this,StatActivity.class);
                    intent.putExtra(MainActivity.QUESTION_KEY,questions);
                    startActivity(intent);
                }
            }

        }.start();
    }

    public void createViewsAndSetParams(Question question){

        if(radioGroup_options.getChildCount() != 0){
            radioGroup_options.removeAllViews();
            //radioGroup_options.clearCheck();
        }
        imageView.setImageBitmap(null);

        tv_questionNum.setText("Q"+Integer.toString(question.getId()+1));
        tv_questionText.setTextColor(Color.BLACK);
        tv_questionText.setText(question.getQuestionText());

        new GetImageAsyncTask(TriviaActivity.this).execute(question.getUrlToImage(),Integer.toString(count));


        for(int i=1;i<=question.getChoices().size();i++){
            RadioButton radioButton = new RadioButton(TriviaActivity.this);
            String choice = question.getChoices().get(i-1);
            radioButton.setText(choice);
            //radioButton.setId(i);
            if(i== question.getUserAnswerIndex()){
                radioButton.setChecked(true);
            }
            radioGroup_options.addView(radioButton);
        }



        if(question.getUserAnswerIndex() !=0){

        }
    }

    @Override
    public void postBitmap(Bitmap bitmap,int id) {
        if(id == count)
        {
            imageView.setImageBitmap(bitmap);
        }
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

    @Override
    public void postQuestions(ArrayList<Question> questions) {

    }
}
