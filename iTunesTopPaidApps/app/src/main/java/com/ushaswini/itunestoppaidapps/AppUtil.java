package com.ushaswini.itunestoppaidapps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
* AppUtil.java
* Vinnakota Venkata Ratna Ushaswini
* Abhishekh Surya*/

public class AppUtil {

    static class AppJSONParser{
        public static ArrayList<App> parseApps(String input) throws JSONException {
            ArrayList<App> appArrayList = new ArrayList<>();

            JSONObject root = new JSONObject(input);
            JSONObject feed = root.getJSONObject("feed");
            JSONArray appJSONArray = feed.getJSONArray("entry");

            for(int i=0;i<appJSONArray.length();i++){
                JSONObject appJSON = appJSONArray.getJSONObject(i);
                App app = new App();

                JSONObject objTitle = appJSON.getJSONObject("title");
                app.setAppName(objTitle.getString("label"));

                JSONObject objPrice = appJSON.getJSONObject("im:price");
                app.setAppPrice(objPrice.getString("label"));

                JSONArray arrayImages = appJSON.getJSONArray("im:image");
                JSONObject objImage = arrayImages.getJSONObject(0);
                app.setThumbUrl(objImage.getString("label"));

                appArrayList.add(app);
            }
            return appArrayList;
        }
    }
}
