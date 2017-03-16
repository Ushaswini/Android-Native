package com.ushaswini.triviaapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by ushas on 08/02/2017.
 */

public interface IShareDataFromAsync {

    void postQuestions(ArrayList<Question> questions);

    public void postBitmap(Bitmap bitmap,int id);

    void updateProgressBar(boolean isComplete);
}
