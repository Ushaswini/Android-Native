/*
* Assignment : 5
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.cnnnewsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ushas on 13/02/2017.
 */

public class GetImageAsyncTask extends AsyncTask<String,Void,Bitmap> {
    IShareDataFromAsync shareData;

    public GetImageAsyncTask(IShareDataFromAsync shareData) {
        this.shareData = shareData;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        shareData.postBitmap(bitmap);
    }
}
