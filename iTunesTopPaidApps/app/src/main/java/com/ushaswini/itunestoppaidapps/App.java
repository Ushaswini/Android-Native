package com.ushaswini.itunestoppaidapps;

import java.io.Serializable;

/*
* App.java
* Vinnakota Venkata Ratna Ushaswini
* Abhishekh Surya*/

public class App implements Serializable {

    String thumbUrl, appName,appPrice;
    boolean isFavorite;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(String appPrice) {
        this.appPrice = appPrice;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public String toString() {
        return appName + System.getProperty ("line.separator") + "Price: "+appPrice;
    }
}
