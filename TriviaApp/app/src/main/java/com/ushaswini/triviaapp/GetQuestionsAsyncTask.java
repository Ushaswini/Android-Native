package com.ushaswini.triviaapp;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ushas on 08/02/2017.
 */

public class GetQuestionsAsyncTask extends AsyncTask<String,Void,ArrayList<Question>>{
    IShareDataFromAsync shareData;

    public GetQuestionsAsyncTask(IShareDataFromAsync shareData) {
        this.shareData = shareData;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line+"\n");
            }
            return QuestionUtil.QuestionJSONParser.parseQuestions(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try{
                if(reader != null){
                    reader.close();
                }
            }catch (IOException oExcep){
                oExcep.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        shareData.updateProgressBar(false);
    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        shareData.updateProgressBar(true);
        shareData.postQuestions(questions);
    }
}
