package com.ushaswini.itunestoppaidapps;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
* GetAppsAsyncTask.java
* Vinnakota Venkata Ratna Ushaswini
* Abhishekh Surya*/

public class GetAppsAsyncTask extends AsyncTask<String,Void,ArrayList<App>> {

    IShareData shareData;

    public GetAppsAsyncTask(IShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    protected ArrayList<App> doInBackground(String... params) {
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = "";

            while((line = reader.readLine()) != null){
                sb.append(line+"\n");
            }

            return AppUtil.AppJSONParser.parseApps(sb.toString());

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
    protected void onPostExecute(ArrayList<App> apps) {

        if(apps != null){
            shareData.postAppArrayList(apps);
        }
    }
}
