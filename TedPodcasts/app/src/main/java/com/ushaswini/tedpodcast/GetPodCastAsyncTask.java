package com.ushaswini.tedpodcast;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * GetPodCastAsyncTask
 * 06/03/2017
 */

public class GetPodCastAsyncTask extends AsyncTask<String,Void,ArrayList<Podcast>> {

    IShareDataFromAsync iShareDataFromAsync;

    public GetPodCastAsyncTask(IShareDataFromAsync iShareDataFromAsync) {
        this.iShareDataFromAsync = iShareDataFromAsync;
    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> podcasts) {
        super.onPostExecute(podcasts);
        if(null != podcasts){
            iShareDataFromAsync.postPodcasts(podcasts);
        }
    }

    @Override
    protected ArrayList<Podcast> doInBackground(String... params) {

        URL url = null;
        try {
            Log.d("URL",params[0]);
            url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                return PodcastUtil.PodcastSAXParser.parsePodcast(input);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;

    }

}
