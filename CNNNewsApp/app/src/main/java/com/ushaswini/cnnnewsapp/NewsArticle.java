/*
* Assignment : 5
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.cnnnewsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ushas on 13/02/2017.
 */

public class NewsArticle {

    String urlToImage, title, description, pubDate, pubDateFormatted;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

        try {
            Date date = fmt.parse(pubDate);

            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.setPubDateFormatted(outFormat.format(date));
        }
        catch(ParseException pe) {

        }
    }

    public String getPubDateFormatted() {
        return pubDateFormatted;
    }

    public void setPubDateFormatted(String pubDateFormatted) {
        this.pubDateFormatted = pubDateFormatted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "description='" + description + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", title='" + title + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", pubDateFormatted=" + pubDateFormatted +
                '}';
    }
}
