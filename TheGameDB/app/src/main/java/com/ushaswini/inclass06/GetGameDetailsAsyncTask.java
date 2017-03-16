package com.ushaswini.inclass06;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by ushas on 20/02/2017.
 */

public class GetGameDetailsAsyncTask extends AsyncTask<RequestParams,Void,GameDetails> {

    IShareData shareData;

    public GetGameDetailsAsyncTask(IShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    protected GameDetails doInBackground(RequestParams... params) {
        HttpURLConnection connection = null;
        try {
            connection = params[0].getConnection();
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                return GameUtil.GameDetailsSaxParser.parseGameDetails(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(GameDetails gameDetails) {

        if(gameDetails !=null){
            super.onPostExecute(gameDetails);
            Log.d("Details",gameDetails.toString());
            shareData.postGameDetails(gameDetails);
        }
    }
}

