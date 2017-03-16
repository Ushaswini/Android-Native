/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
package com.ushaswini.inclass06;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by ushas on 20/02/2017.
 */

public class RequestParams {

    String baseUrl,method;
    HashMap<String,String> params = new HashMap<String, String>();

    public RequestParams(String baseUrl, String method) {
        this.baseUrl = baseUrl;
        this.method = method;
    }

    public void addParam(String key, String value){
        params.put(key,value);
    }

    public String getEncodedUrl(){
        return this.baseUrl + "?" +getEncodedParams();
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

    public HttpURLConnection getConnection() throws IOException {

        String encodedUrl = getEncodedUrl();
        Log.d("encoded url",encodedUrl);

        URL url = new URL(getEncodedUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(this.method);
        return con;
    }
}

