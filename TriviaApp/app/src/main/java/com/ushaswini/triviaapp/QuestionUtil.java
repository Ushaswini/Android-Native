package com.ushaswini.triviaapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ushas on 08/02/2017.
 */

public class QuestionUtil {

    public static class QuestionJSONParser{

        public static ArrayList<Question> parseQuestions(String input ) throws JSONException {

            ArrayList<Question> questionsList = new ArrayList<Question>();
            JSONObject root = new JSONObject(input);
            JSONArray questionsJSONArray = root.getJSONArray("questions");

            for (int i = 0; i < questionsJSONArray.length(); i++) {

                JSONObject questionJSON = questionsJSONArray.getJSONObject(i);
                Question question = new Question();
                question.setId(questionJSON.getInt("id"));

                question.setQuestionText(questionJSON.getString("text"));

                JSONObject choicesObj = questionJSON.getJSONObject("choices");

                question.setAnswerIndex(choicesObj.getInt("answer"));

                ArrayList<String> choices = new ArrayList<String>();
                JSONArray choicesJSONArray = choicesObj.getJSONArray("choice");
                for(int j=0;j<choicesJSONArray.length();j++){
                    String choice  = choicesJSONArray.getString(j);
                    choices.add(choice);
                }
                question.setChoices(choices);

                if(questionJSON.has("image")){
                    question.setUrlToImage(questionJSON.getString("image"));
                }

                questionsList.add(question);

            }

            return questionsList;
        }
    }
}
