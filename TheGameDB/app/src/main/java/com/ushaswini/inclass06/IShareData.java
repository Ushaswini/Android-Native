/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.inclass06;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by ushas on 20/02/2017.
 */

public interface IShareData {

    void postGamesList(ArrayList<GameBasic> gameArrayList);
    void postGameDetails(GameDetails gameDetails);
    void postBitmap(Bitmap image);
    void postImageUrl(String url);
}

