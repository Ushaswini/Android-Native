package com.ushaswini.homework09;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * GetCityDetailsAsyncTask
 * 03/04/2017
 */

public class GetCityDetailsAsyncTask extends AsyncTask<RequestParams,Void,CityDetails> {

    IShareData iShareData;

    public GetCityDetailsAsyncTask(IShareData iShareData) {
        this.iShareData = iShareData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iShareData.updateProgressBar(true);
    }

    @Override
    protected CityDetails doInBackground(RequestParams... params) {

        BufferedReader reader = null;
        try {
            HttpURLConnection connection = params[0].getLocationConnection();
            String country = params[0].getCountry();

            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while((line = reader.readLine()) != null){
                    sb.append(line+"\n");
                }

                return JSONParserUtil.JSONParser.parseLocation(sb.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(CityDetails cityDetails) {
        super.onPostExecute(cityDetails);
        iShareData.updateProgressBar(false);
        iShareData.postLocation(cityDetails);

    }
}
