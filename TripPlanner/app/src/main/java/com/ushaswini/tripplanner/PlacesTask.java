package com.ushaswini.tripplanner;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * PlacesTask
 * 29/04/2017
 */

// Fetches all places from GooglePlaces AutoComplete Web Service
 class PlacesTask extends AsyncTask<String, Void, String> {

    ParserTask parserTask;

    ISharePlaces iSharePlaces;

    public PlacesTask(ISharePlaces iSharePlaces) {
        this.iSharePlaces = iSharePlaces;
    }

    @Override
    protected String doInBackground(String... place) {
        // For storing data from web service
        String data = "";

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAJI7ReMzRbUCO_ZUd0iQzNsFMu3ooousc";

        String input = "";

        try {
            input = "input=" + URLEncoder.encode(place[0], "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        try {
            // Fetching the data from we service
            data = downloadUrl(url);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Creating ParserTask
        parserTask = new ParserTask(iSharePlaces);

        // Starting Parsing the JSON string returned by Web Service
        parserTask.execute(result);
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("demo", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

/** A class to parse the Google Places in JSON format */
 class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

    JSONObject jObject;

    ISharePlaces iSharePlaces;

    public ParserTask(ISharePlaces iSharePlaces) {
        this.iSharePlaces = iSharePlaces;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(String... jsonData) {

        List<HashMap<String, String>> places = null;

        PlaceJSONParser placeJsonParser = new PlaceJSONParser();

        try{
            jObject = new JSONObject(jsonData[0]);

            // Getting the parsed data as a List construct
            places = placeJsonParser.parse(jObject);

        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        return places;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> result) {


        iSharePlaces.postPlacesResult(result);

        /*String[] from = new String[] { "description"};
        int[] to = new int[] { android.R.id.text1 };

        // Creating a SimpleAdapter for the AutoCompleteTextView
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

        // Setting the adapter
        atvPlaces.setAdapter(adapter);*/
    }


}

interface  ISharePlaces{
    void postPlacesResult(List<HashMap<String, String>> result);
}
