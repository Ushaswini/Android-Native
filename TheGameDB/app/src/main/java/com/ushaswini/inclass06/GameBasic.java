/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.inclass06;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ushas on 20/02/2017.
 */

public class GameBasic implements Serializable
{
    String gameTitle,releaseDate="",platform,imageUrl,baseImageUrl;
    int id;

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = fmt.parse(releaseDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy");
            this.releaseDate = outFormat.format(date);
            //Log.d("date",this.releaseDate);
        }
        catch(ParseException pe) {
            this.releaseDate = "";
        }
        //this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }

    @Override
    public String toString() {
        return  gameTitle +
                ", Released in " + releaseDate +
                ", Platform: " + platform + '.' ;
    }
}
