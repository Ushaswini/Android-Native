/*
* App.java
* Vinnakota Venkata Ratna Ushaswini
* Abhishekh Surya*/

package com.ushaswini.itunestoppaidapps;
/*
* FavoritesActivity.java
* Vinnakota Venkata Ratna Ushaswini
* Abhishekh Surya*/
import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    ListView list_favorite_apps;
    Button btn_finish;
    ArrayList<App>appsList,favoriteAppsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Favorites Apps");
        setContentView(R.layout.activity_favorites);

        list_favorite_apps = (ListView) findViewById(R.id.list_favorite_apps);
        btn_finish = (Button) findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appsList = new ArrayList<>();
        favoriteAppsList = new ArrayList<>();

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(MainActivity.FAVORITES_KEY)){
                appsList = (ArrayList<App>) getIntent().getExtras().get(MainActivity.FAVORITES_KEY);

                final SharedPreferences myPrefs = getSharedPreferences("com.ushaswini.itunestoppaidapps", MODE_PRIVATE);
                final Set<String> nullSet = new ArraySet<String>();
                final Set<String> set = myPrefs.getStringSet("app_favorites",nullSet);
                for (App app:appsList) {
                    if(set.contains(app.getAppName())){
                        favoriteAppsList.add(app);
                        Log.d("app fav act",app.toString());
                    }
                }
                if(favoriteAppsList.size()>0){
                    AppsArrayAdapter arrayAdapter = new AppsArrayAdapter(this,R.layout.list_row_item,favoriteAppsList);
                    list_favorite_apps.setAdapter(arrayAdapter);
                }

            }
        }
    }
}
