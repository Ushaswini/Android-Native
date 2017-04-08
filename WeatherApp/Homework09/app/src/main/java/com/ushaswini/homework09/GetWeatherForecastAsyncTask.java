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
 * GetWeatherForecastAsyncTask
 * 04/04/2017
 */

public class GetWeatherForecastAsyncTask extends AsyncTask<RequestParams,Void,FiveDayForecasts> {

    IShareData iShareData;

    public GetWeatherForecastAsyncTask(IShareData iShareData) {
        this.iShareData = iShareData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected FiveDayForecasts doInBackground(RequestParams... params) {

        BufferedReader reader = null;

        try {
            // Log.d("encoded url",params[0].getEncodedCurrentForecastUrl());
            HttpURLConnection connection = params[0].getWeatherConnection();
            connection.connect();

            int statusCode = connection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while((line = reader.readLine()) != null){
                    sb.append(line+"\n");
                }
                FiveDayForecasts forecasts = JSONParserUtil.JSONParser.parseForecasts(sb.toString());


                return forecasts;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(FiveDayForecasts forecasts) {
        super.onPostExecute(forecasts);
        iShareData.postForecasts(forecasts);
    }
}
