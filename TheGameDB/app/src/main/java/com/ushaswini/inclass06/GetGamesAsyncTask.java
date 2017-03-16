/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
package com.ushaswini.inclass06;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by ushas on 20/02/2017.
 */

public class GetGamesAsyncTask extends AsyncTask<RequestParams,Void,ArrayList<GameBasic>> {

    IShareData shareData;

    public GetGamesAsyncTask(IShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    protected void onPostExecute(ArrayList<GameBasic> games) {
        super.onPostExecute(games);
        if(games != null){
            Log.d("Games",games.toString());
            shareData.postGamesList(games);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<GameBasic> doInBackground(RequestParams... params) {
        HttpURLConnection connection = null;
        try {
            connection = params[0].getConnection();
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                return GameUtil.GameBasicSaxParser.parseGameBasic(inputStream);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        return null;
    }
}

