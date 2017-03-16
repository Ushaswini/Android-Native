package com.ushaswini.inclass06;

/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ushas on 20/02/2017.
 */

public class GameDetails extends GameBasic implements Serializable {

    String baseUrl,overview,youtube,publisher,genre;
    int similarCount;

    ArrayList<Integer> similarGamesId;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getSimilarCount() {
        return similarCount;
    }

    public void setSimilarCount(int similarCount) {
        this.similarCount = similarCount;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public ArrayList<Integer> getSimilarGamesId() {
        return similarGamesId;
    }

    public void setSimilarGamesId(ArrayList<Integer> similarGames) {
        this.similarGamesId = similarGames;
    }

}
