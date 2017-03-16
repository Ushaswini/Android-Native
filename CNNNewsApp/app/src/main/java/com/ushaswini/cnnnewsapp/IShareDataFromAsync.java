/*
* Assignment : 5
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.cnnnewsapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by ushas on 13/02/2017.
 */

public interface IShareDataFromAsync {

    public void postBitmap(Bitmap bitmap);
    public void postArticles(ArrayList<NewsArticle> articles);
    public void updateProgressBar(boolean isComplete);

}
