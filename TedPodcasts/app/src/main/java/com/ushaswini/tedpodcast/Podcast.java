package com.ushaswini.tedpodcast;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * Podcast
 * 06/03/2017
 */

public class Podcast implements Serializable , Comparable<Podcast> {

    String episodeTitle,imageUrl,pubDateStr,description,mp3Url,duration;
    Date pubDate;

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPubDateStr() {
        return pubDateStr;
    }

    public void setPubDateStr(String pubDateStr) {
        this.pubDateStr = pubDateStr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "episodeTitle='" + episodeTitle + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", pubDateStr='" + pubDateStr + '\'' +
                ", description='" + description + '\'' +
                ", mp3Url='" + mp3Url + '\'' +
                ", duration='" + duration + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }

    @Override
    public int compareTo(@NonNull Podcast o) {
        if(this.getPubDate() == null || o.getPubDate() == null){
            return 0;
        }else{
            Log.d("episodeTitle",episodeTitle);
            //Log.d("imageUrl",imageUrl);
            return pubDate.compareTo(o.getPubDate());
        }
    }

    public static Comparator<Podcast> PodcastDateComparator = new Comparator<Podcast>() {
        @Override
        public int compare(Podcast o1, Podcast o2) {

            if (o1.getPubDate() == null || o2.getPubDate() == null)
                return 0;
            Date date1 = o1.getPubDate();
            Date date2 = o2.getPubDate();

            return date2.compareTo(date1);
        }
    };
}
