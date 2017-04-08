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
 * GetCityWeatherAsyncTask
 * 04/04/2017
 */

public class GetCityWeatherAsyncTask extends AsyncTask<RequestParams,Void,Weather> {

    IShareData iShareData;

    public GetCityWeatherAsyncTask(IShareData iShareData) {
        this.iShareData = iShareData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //iShareData.updateProgressBar(true);
    }

    @Override
    protected Weather doInBackground(RequestParams... params) {

        BufferedReader reader = null;

        try {
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
                Weather weather = JSONParserUtil.JSONParser.parseWeather(sb.toString());
                weather.setCity(params[0].getCity());
                weather.setCountry(params[0].getCountry());

                return weather;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);

        if(weather != null){
            iShareData.postWeather(weather);
        }
    }
}
