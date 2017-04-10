package com.ushaswini.homework09;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IShareData, CurrentCityNotSet.OnSetCurrentCityFragmentListener{

    EditText et_city;
    EditText et_country;
    Button btn_search;
    RecyclerView rv_savedCities;
    ProgressBar progressBar;
    TextView tv_status;


    SavedCitiesCustomAdapter adapter;


    final static String PREF_CITY_TAG = "current_city";
    final static String PREF_KEY_TAG = "city_key";
    final static String PREF_COUNTRY_TAG = "country";

    final static String BASE_URL = "http://dataservice.accuweather.com";
    final static String LOCATION_URL = BASE_URL + "/locations/v1/";
    final static String CURRENT_FORECAST_URL = BASE_URL + "/currentconditions/v1/";
    final static String DAYS_5_URL =BASE_URL + "/forecasts/v1/daily/5day/";
    final static String WEATHER_ICON_URL = "http://developer.accuweather.com/sites/default/files/";
    final static String DEGREE_CELECIUS_SIGN_UNICODE = "\u2103";
    final static String DEGREE_FAHRENHEIT_SIGN_UNICODE = "\u2109";
    final static String DEGREE_SIGN_UNICODE = "\u00b0";


    final static String API_KEY = "wfYmrkzVTOmGHaObnqRCVOW6ABmtae8d";

    final static String CITY_TAG = "city";
    final static String COUNTRY_TAG = "country";

    SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;
    String pref_city;
    String pref_city_key;
    String pref_country;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mWeatherRef = mRootRef.child("CityWeather");
    ArrayList<Weather> savedCitiesWeatherDetails = new ArrayList<>();
    ArrayList<Weather> favoriteCities = new ArrayList<>();
    ArrayList<Weather> notFavoriteCities = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Weather App");
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.accu_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        et_city = (EditText) findViewById(R.id.et_city);
        et_country = (EditText) findViewById(R.id.et_country);
        btn_search = (Button) findViewById(R.id.btn_search);
        rv_savedCities = (RecyclerView) findViewById(R.id.rv_savedCities);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_status = (TextView) findViewById(R.id.tv_status);
    }

    @Override
    protected void onStart() {

        super.onStart();
        btn_search.setOnClickListener(btn_search_click);

        mWeatherRef.addValueEventListener(eventListener);


        adapter = new SavedCitiesCustomAdapter(savedCitiesWeatherDetails,MainActivity.this);
        rv_savedCities.setAdapter(adapter);
        rv_savedCities.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        pref_city = myPrefs.getString(PREF_CITY_TAG,"");
        pref_city_key = myPrefs.getString(PREF_KEY_TAG,"");
        pref_country = myPrefs.getString(PREF_COUNTRY_TAG,"");

        if(pref_city.equals("")){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,new CurrentCityNotSet(),"NOT_SET")
                    .addToBackStack(null)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }else{
            progressBar.setVisibility(View.GONE);

            RequestParams params = new RequestParams(CURRENT_FORECAST_URL,"GET");

            params.setCountry(pref_country);
            params.setCity(pref_city);

            params.setCity_key(pref_city_key);
            params.addParam("apikey",API_KEY);

            new GetCityWeatherAsyncTask(MainActivity.this).execute(params);


        }
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            long countOfSavedCities = dataSnapshot.child("Saved Cities").getChildrenCount();
            Log.d("child count",countOfSavedCities + "");
            savedCitiesWeatherDetails.clear();
            favoriteCities.clear();
            notFavoriteCities.clear();

            if(countOfSavedCities > 0){
                //tv_status.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : dataSnapshot.child("Saved Cities").getChildren()) {
                    Weather cityDetails = postSnapshot.getValue(Weather.class);
                    if(cityDetails.isFavorite()){
                        favoriteCities.add(cityDetails);
                    }else{
                        notFavoriteCities.add(cityDetails);
                    }
                    Log.d("saved cities",cityDetails.toString());
                }
                savedCitiesWeatherDetails.addAll(favoriteCities);
                savedCitiesWeatherDetails.addAll(notFavoriteCities);
                adapter.notifyDataSetChanged();
                tv_status.setText("Saved Cities");


            }else {
                tv_status.setText(R.string.NoCityToDisplay);
                //tv_status.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    View.OnClickListener btn_search_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String city = et_city.getText().toString();
            String country = et_country.getText().toString();

            if(city.equals("") || country.equals("")){
                Toast.makeText(MainActivity.this,"Enter complete details.",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(MainActivity.this,CityWeatherActivity.class);
                intent.putExtra(CITY_TAG,city);
                intent.putExtra(COUNTRY_TAG,country);
                startActivity(intent);
            }




        }
    };


    @Override
    public void postLocation(CityDetails cityDetails) {

        String output = "";
        //Log.d("output", cityDetails.getKey().toString());
        if(cityDetails != null && !cityDetails.getKey().equals("")){
            output = "Current City details saved";


            prefsEditor = myPrefs.edit();

            prefsEditor.clear().commit();


            prefsEditor.putString(PREF_CITY_TAG,cityDetails.getCity());
            prefsEditor.putString(PREF_COUNTRY_TAG,cityDetails.getCountry());
            prefsEditor.putString(PREF_KEY_TAG,cityDetails.getKey());

            prefsEditor.commit();


            RequestParams params = new RequestParams(CURRENT_FORECAST_URL,"GET");

            params.setCountry(cityDetails.getCountry());
            params.setCity(cityDetails.getCity());

            params.setCity_key(cityDetails.getKey());
            params.addParam("apikey",API_KEY);

            new GetCityWeatherAsyncTask(MainActivity.this).execute(params);

        }else{
            output = "City not found";
        }
        Toast.makeText(MainActivity.this,output,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateProgressBar(boolean show) {

        if(show){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void postWeather(Weather weather) {

        Log.d("Weather",weather.toString());

        progressBar.setVisibility(View.GONE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,new CurrentCitySet(),"SET")
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        CurrentCitySet f = (CurrentCitySet) getSupportFragmentManager().findFragmentByTag("SET");
        if(f != null){
            f.postCityWeatherDetails(weather);
        }
    }

    @Override
    public void updateFirebaseForFavorite(Weather cityDetails) {


        Log.d("weather after update",cityDetails.toString());
        mWeatherRef.child("Saved Cities").child(cityDetails.getCity()).child("isFavorite").setValue(cityDetails.isFavorite());


    }

    @Override
    public void postForecasts(FiveDayForecasts forecasts) {

    }

    @Override
    public void handleItemClick(int count) {

    }

    @Override
    public void handleLongItemClick(String cityToDelete) {
        mWeatherRef.child("Saved Cities").child(cityToDelete).removeValue();
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(MainActivity.this,PreferenceActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onSetCityButtonClicked() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogview = inflater.inflate(R.layout.alert_dialog_inputs, null);

        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);

        dialogbuilder.
                setTitle("Enter City Details")
                .setView(dialogview);

        final AlertDialog dialog = dialogbuilder.create();

        final EditText et_city = (EditText) dialogview.findViewById(R.id.et_city);
        final EditText et_country = (EditText) dialogview.findViewById(R.id.et_country);
        final Button btn_set = (Button) dialogview.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button)dialogview.findViewById(R.id.btn_cancel);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;


                String city = et_city.getText().toString();
                String country = et_country.getText().toString();


                if(city.equals("") || country.equals("")){
                    Toast.makeText(MainActivity.this,"Enter complete details.",Toast.LENGTH_SHORT).show();
                }else {
                    RequestParams params = new RequestParams(LOCATION_URL,"GET");
                    params.setCity(city);
                    params.setCountry(country);

                    params.addParam("apikey",API_KEY);
                    params.addParam("q",et_city.getText().toString());

                    new GetCityDetailsAsyncTask(MainActivity.this).execute(params);
                    dialog.dismiss();

                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
