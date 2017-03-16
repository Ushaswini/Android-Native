/*
* Assignment : 5
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.cnnnewsapp;

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
 * Created by ushas on 13/02/2017.
 */

public class GetNewsAsyncTask extends AsyncTask<String,Void,ArrayList<NewsArticle>>{
    IShareDataFromAsync shareData;

    public GetNewsAsyncTask(IShareDataFromAsync shareData) {
        this.shareData = shareData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        shareData.updateProgressBar(false);
    }

    @Override
    protected ArrayList<NewsArticle> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream input = connection.getInputStream();
                return NewsArticleUtil.ArticleSAXParser.parseNewsArticle(input);
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

    @Override
    protected void onPostExecute(ArrayList<NewsArticle> newsArticles) {
        super.onPostExecute(newsArticles);
        if(newsArticles != null){
            shareData.updateProgressBar(true);
            shareData.postArticles(newsArticles);

            Log.d("demo",newsArticles.toString());
            Log.d("demo",Integer.toString(newsArticles.size()));
        }
    }
}
