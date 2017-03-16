package com.ushaswini.itunestoppaidapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
* MainActivity.java
* Vinnakota Venkata Ratna Ushaswini
* Abhishekh Surya*/

public class MainActivity extends AppCompatActivity implements IShareData {

    ListView list_apps;
    ProgressDialog pd_loading;

    ArrayList<App> appArrayList;

    AppsArrayAdapter arrayAdapter;

    private static final String BASE_URL = " https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json";
    public static final String FAVORITES_KEY = "FAVORITES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("iTunes Top Paid Apps");

        setContentView(R.layout.activity_main);
        pd_loading = new ProgressDialog(MainActivity.this);

        list_apps = (ListView) findViewById(R.id.list_apps);
        pd_loading.setTitle("Loading");
        pd_loading.show();

        new GetAppsAsyncTask(this).execute(BASE_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.refresh:
                Log.d("Item","Refresh Button is selected");
                pd_loading.show();
                new GetAppsAsyncTask(MainActivity.this).execute(BASE_URL);
                return  true;

            case R.id.favorites:
                Log.d("Item","Favorites Button is selected");
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                intent.putExtra(FAVORITES_KEY, appArrayList);
                startActivity(intent);
                return  true;

            case R.id.sort_increasingly:
                Log.d("Item","Sort Increasingly Button is selected");
                Collections.sort(appArrayList,INCREASING_PRICE);
                arrayAdapter.notifyDataSetChanged();
                return  true;

            case R.id.sort_decreasingly:
                Log.d("Item","Sort Decreasingly Button is selected");
                Collections.sort(appArrayList,DECREASING_PRICE);
                arrayAdapter.notifyDataSetChanged();
                return  true;

            default:
                Log.d("Item","Default");
                return super.onOptionsItemSelected(item);
        }
    }

    Comparator<App> INCREASING_PRICE = new Comparator<App>() {
        @Override
        public int compare(App o1, App o2) {
            Double price1 = Double.parseDouble(o1.getAppPrice().replace("$",""));
            Double price2 = Double.parseDouble(o2.getAppPrice().replace("$",""));

            if(price1 > price2){
                return 1;
            }else if(price1 < price2){
                return -1;
            }else{
                return 0;
            }
        }
    };

    Comparator<App> DECREASING_PRICE = new Comparator<App>() {
        @Override
        public int compare(App o1, App o2) {
            Double price1 = Double.parseDouble(o1.getAppPrice().replace("$",""));
            Double price2 = Double.parseDouble(o2.getAppPrice().replace("$",""));

            if(price1 > price2){
                return -1;
            }else if(price1 < price2){
                return 1;
            }else{
                return 0;
            }
        }
    };



    @Override
    public void postAppArrayList(ArrayList<App> apps) {
        Log.d("postAppArrayList", "Apps array received");
        for(int i = 0;i<apps.size();i++){
            Log.d("app", apps.get(i).toString());
        }
        appArrayList = apps;

        arrayAdapter = new AppsArrayAdapter(this,R.layout.list_row_item,appArrayList);
        list_apps.setAdapter(arrayAdapter);
        pd_loading.dismiss();
    }
}
