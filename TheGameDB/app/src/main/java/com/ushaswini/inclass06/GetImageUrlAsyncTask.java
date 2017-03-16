/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.inclass06;

import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by ushas on 20/02/2017.
 */

public class GetImageUrlAsyncTask extends AsyncTask<RequestParams,Void,String> {
    IShareData shareData;

    public GetImageUrlAsyncTask(IShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    protected String doInBackground(RequestParams... params) {
        HttpURLConnection connection = null;
        try {
            connection = params[0].getConnection();
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                return GameUtil.GameImageUrlSaxParser.parseGameImageUrl(inputStream);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        shareData.postImageUrl(s);
    }
}
