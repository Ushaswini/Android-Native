package com.ushaswini.homework09;

import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;


public class RequestParams {

    String url;
    String method;
    String country;
    String city_key;
    String city;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    HashMap<String,String> params = new HashMap<String, String>();


    public RequestParams(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getCity_key() {
        return city_key;
    }

    public void setCity_key(String city_key) {
        this.city_key = city_key;
    }



    public void addParam(String key, String value){
        params.put(key,value);
    }

    public String getEncodedLocationUrl(){
        Log.d("encoded Location url",this.url + country + "/search"+ "?" +getEncodedParams());

        return this.url + country + "/search"+ "?" +getEncodedParams();
    }

    public String getEncodedCurrentForecastUrl(){
        return this.url + city_key +"?"+ getEncodedParams();
    }

    public String get5DayForecastUrl(){
        return this.url + city_key + "?" + getEncodedParams();

    }

    public String getEncodedParams(){

        StringBuilder sb = new StringBuilder();

        for(String key : params.keySet()){
            try {
                String value = URLEncoder.encode(params.get(key),"UTF-8");
                if(sb.length() >0){
                    sb.append("&");
                }
                sb.append(key+"="+value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public HttpURLConnection getLocationConnection() throws IOException {

        URL url = new URL(getEncodedLocationUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(this.method);
        return con;
    }

    public HttpURLConnection getWeatherConnection() throws IOException {
        String encodedUrl = getEncodedCurrentForecastUrl();
        Log.d("encoded url",encodedUrl);

        URL url = new URL(getEncodedCurrentForecastUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(this.method);
        return con;
    }
}
