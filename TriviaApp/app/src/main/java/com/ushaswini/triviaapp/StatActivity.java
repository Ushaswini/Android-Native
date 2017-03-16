package com.ushaswini.triviaapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StatActivity extends AppCompatActivity {

    Button btn_finish;
    LinearLayout linear_answers;
    ProgressBar progressBar;
    TextView tv_percentage;
    ArrayList<Question>questions;

    float wrongAnswerCount;
    float percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        setTitle("Trivia Stats");

        btn_finish = (Button) findViewById(R.id.btn_finish);
        linear_answers = (LinearLayout) findViewById(R.id.linear_answers);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        tv_percentage = (TextView) findViewById(R.id.tv_percentage);

        if(getIntent().getExtras() != null){
           questions = getIntent().getExtras().getParcelableArrayList(MainActivity.QUESTION_KEY);
            if(questions !=null){
                for (Question question : questions) {
                    if(!question.isCorrectAnswer){
                        wrongAnswerCount++;
                        String userAnswer;
                        if(question.getUserAnswerIndex() >0){
                           userAnswer  = question.getChoices().get(question.getUserAnswerIndex()-1);
                        }else {
                            userAnswer = "Not Attempted";
                        }
                        String correctAnswer = question.getChoices().get(question.getAnswerIndex()-1);
                        createTextViews(question.getQuestionText(),userAnswer,correctAnswer);
                    }

                }
                for (Question question:questions) {
                    if(question.isCorrectAnswer){
                        String correctAnswer = question.getChoices().get(question.getUserAnswerIndex()-1);
                        createTextViews(question.getQuestionText(),"",correctAnswer);
                    }

                }
            }
        }

        percentage = (float)((questions.size()-wrongAnswerCount)/questions.size())*100;
        progressBar.setProgress(Math.round(percentage));
        tv_percentage.setText(Float.toString(percentage)+"%");
        tv_percentage.setTextColor(Color.BLACK);


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
                startActivity(intent);
            }
        });
    }

    private void createTextViews(String questionText,String userAnswer,String correctAnswer){

        TextView tv_question = new TextView(this);
        tv_question.setText(questionText);
        tv_question.setTextColor(Color.BLACK);


        TextView tv_userAnswer = new TextView(this);

        TextView tv_correctAnswer = new TextView(this);
        tv_correctAnswer.setText(correctAnswer);
        tv_correctAnswer.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,30);
        tv_correctAnswer.setLayoutParams(params);


        linear_answers.addView(tv_question);
        if(!"".equals(userAnswer)){
            tv_userAnswer.setText(userAnswer);
            tv_userAnswer.setBackgroundColor(Color.RED);
            tv_userAnswer.setTextColor(Color.BLACK);
            linear_answers.addView(tv_userAnswer);
        }
        linear_answers.addView(tv_correctAnswer);
    }
}
