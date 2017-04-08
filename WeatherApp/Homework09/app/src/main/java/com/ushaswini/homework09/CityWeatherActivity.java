package com.ushaswini.homework09;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ushaswini.homework09.MainActivity.API_KEY;
import static com.ushaswini.homework09.MainActivity.CURRENT_FORECAST_URL;
import static com.ushaswini.homework09.MainActivity.DAYS_5_URL;
import static com.ushaswini.homework09.MainActivity.DEGREE_CELECIUS_SIGN_UNICODE;
import static com.ushaswini.homework09.MainActivity.DEGREE_SIGN_UNICODE;
import static com.ushaswini.homework09.MainActivity.LOCATION_URL;
import static com.ushaswini.homework09.MainActivity.PREF_CITY_TAG;
import static com.ushaswini.homework09.MainActivity.PREF_COUNTRY_TAG;
import static com.ushaswini.homework09.MainActivity.PREF_KEY_TAG;
import static com.ushaswini.homework09.MainActivity.WEATHER_ICON_URL;

public class CityWeatherActivity extends AppCompatActivity implements IShareData {

    String city = "";
    String country = "";
    String city_key = "";
    boolean saved_child_exists;
    boolean current_child_exists;

    ProgressDialog progressDialog;

    TextView tv_title;
    TextView tv_heading;
    TextView tv_date;
    TextView tv_temp;
    ImageView im_day;
    ImageView im_night;
    TextView tv_day_comment;
    TextView tv_night_comment;
    Button btn_more_details;
    Button btn_more_forecasts;
    RecyclerView rv_forecasts;

    ArrayList<DailyForecasts> dailyForecasts;
    ForecastAdapter adapter;

    SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;

    int current;
    String mobile_link;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mWeatherRef = mRootRef.child("CityWeather");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        progressDialog = new ProgressDialog(CityWeatherActivity.this);
        progressDialog.setProgress(0);
        progressDialog.setMessage("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Weather App");
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.accu_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        tv_title = (TextView) findViewById(R.id.tv_city);
        tv_heading = (TextView) findViewById(R.id.tv_heading);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_temp = (TextView) findViewById(R.id.tv_updated);
        tv_day_comment = (TextView) findViewById(R.id.tv_day_comment);
        tv_night_comment = (TextView) findViewById(R.id.tv_night_comment);
        im_day = (ImageView) findViewById(R.id.im_day);
        im_night = (ImageView) findViewById(R.id.im_night);
        btn_more_details = (Button) findViewById(R.id.btn_more_details);
        btn_more_forecasts = (Button) findViewById(R.id.btn_extended);
        rv_forecasts = (RecyclerView) findViewById(R.id.rv_forecasts);

        dailyForecasts = new ArrayList<>();

        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(getIntent().getExtras().containsKey(MainActivity.CITY_TAG) && getIntent().getExtras().containsKey(MainActivity.COUNTRY_TAG)){
            if((getIntent().getExtras().getString(MainActivity.CITY_TAG) != null) && (getIntent().getExtras().getString(MainActivity.COUNTRY_TAG) != null)){
                city = getIntent().getExtras().getString(MainActivity.CITY_TAG);
                country = getIntent().getExtras().getString(MainActivity.COUNTRY_TAG);

                tv_title.setText("Daily forecast for " + city + ", " + country);

                RequestParams params = new RequestParams(LOCATION_URL,"GET");
                params.setCity(city);
                params.setCountry(country);

                params.addParam("apikey",API_KEY);
                params.addParam("q",city);

                adapter = new ForecastAdapter(CityWeatherActivity.this,dailyForecasts);
                rv_forecasts.setAdapter(adapter);
                rv_forecasts.setLayoutManager(new LinearLayoutManager(CityWeatherActivity.this,LinearLayoutManager.HORIZONTAL,false));

                mWeatherRef.addValueEventListener(eventListener);

                new GetCityDetailsAsyncTask(CityWeatherActivity.this).execute(params);
            }
        }
        //TODO Month format
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn_more_details.setOnClickListener(more_details);
        btn_more_forecasts.setOnClickListener(extended_forecasts);
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.child("Saved Cities").hasChild(city)){
                saved_child_exists = true;
            }
            if(dataSnapshot.child("Current City").hasChild(city)){
                current_child_exists = true;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    View.OnClickListener more_details = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = dailyForecasts.get(current).getMobile_link();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));

            startActivity(intent);
        }
    };

    View.OnClickListener extended_forecasts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mobile_link));

            startActivity(intent);
        }
    };

    @Override
    public void postLocation(CityDetails cityDetails) {

        if(cityDetails != null && !cityDetails.getKey().equals("")){

            RequestParams params = new RequestParams(DAYS_5_URL,"GET");

            params.setCountry(cityDetails.getCountry());
            params.setCity(cityDetails.getCity());

            city_key = cityDetails.getKey();

            params.setCity_key(city_key);
            params.addParam("apikey",API_KEY);

            new GetWeatherForecastAsyncTask(CityWeatherActivity.this).execute(params);

        }else{
            progressDialog.cancel();
            Toast.makeText(CityWeatherActivity.this,"City not found",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void updateProgressBar(boolean show) {
        if(!show){
            progressDialog.cancel();
        }
    }

    @Override
    public void postWeather(Weather weather) {

        mWeatherRef.child("Saved Cities").child(city).setValue(weather);
        Toast.makeText(CityWeatherActivity.this,"City Saved",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateFirebaseForFavorite(Weather weather) {

    }

    @Override
    public void postForecasts(FiveDayForecasts forecasts) {
        progressDialog.cancel();
        if(forecasts != null){
            Log.d("daily forecasts before",dailyForecasts.toString());

            ArrayList<DailyForecasts> daily = forecasts.getDailyForecasts();
            dailyForecasts.clear();
            dailyForecasts.addAll(daily);

            Log.d("daily forecasts after",dailyForecasts.toString());

            adapter.notifyDataSetChanged();

            tv_heading.setText(forecasts.getHeadline());
            mobile_link = forecasts.getHeadline_mobileLink();

            setParams(dailyForecasts.get(0));

        }
    }

    private void setParams(DailyForecasts forecast){


        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = format.parse(forecast.getDate());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd , yyyy", Locale.US);
            String dateText =  "Forecast on " + dateFormat.format(date);

            tv_date.setText(dateText);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        String temperature;

        if(myPrefs.getString("temperatureUnit", "0").equals("0")){
            Log.d("Forecast",forecast.toString());
            //convert to C
            float temp_min_cel = ((Float.parseFloat(forecast.getMinimum().getValue()) - 32) * 5)/9;
            float temp_max_cel = ((Float.parseFloat(forecast.getMaximum().getValue()) - 32) * 5)/9;

            Log.d("minimum",temp_min_cel+"");
            Log.d("maximum",temp_max_cel+"");

            BigDecimal bd_min = new BigDecimal(Float.toString(temp_min_cel));
            bd_min = bd_min.setScale(1, BigDecimal.ROUND_CEILING);
            BigDecimal bd_max = new BigDecimal(Float.toString(temp_max_cel));
            bd_max = bd_max.setScale(1, BigDecimal.ROUND_CEILING);

            temperature = "Temperature " + bd_max.floatValue()+ DEGREE_SIGN_UNICODE +  "/" + bd_min.floatValue() +DEGREE_SIGN_UNICODE;

        }else{
            temperature = "Temperature " + forecast.getMaximum().getValue() + DEGREE_SIGN_UNICODE + "/" + forecast.getMinimum().getValue() + DEGREE_SIGN_UNICODE;
        }


        tv_temp.setText(temperature);

        //Log.d("image",WEATHER_ICON_URL + forecast.getDay().getIcon() + "-s.png" );
        String icon_day = forecast.getDay().getIcon();
        String icon_night = forecast.getNight().getIcon();

        if(Integer.valueOf(icon_day) < 10){
            icon_day = "0"+icon_day;
        }

        if(Integer.valueOf(icon_night) < 10){
            icon_night = "0"+icon_night;
        }

        Picasso.with(CityWeatherActivity.this)
                .load(WEATHER_ICON_URL + icon_day + "-s.png")
                .into(im_day);
        tv_day_comment.setText(forecast.getDay().getIcon_phrase());

        Picasso.with(CityWeatherActivity.this)
                .load(WEATHER_ICON_URL + icon_night + "-s.png")
                .into(im_night);
        tv_night_comment.setText(forecast.getNight().getIcon_phrase());
    }

    @Override
    public void handleItemClick(int count)
    {
        this.current = count;
        setParams(dailyForecasts.get(count));
    }

    @Override
    public void handleLongItemClick(String city) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("city",city);            Log.d("city_key",city_key);
        Log.d("Country",country);


        switch(item.getItemId()){

            case R.id.action_save:
                Calendar c = Calendar.getInstance();
                Date d = c.getTime();
                SimpleDateFormat format = new SimpleDateFormat();
                String dateToSave = format.format(d);

                float temp_min_cel = ((Float.parseFloat(dailyForecasts.get(0).getMaximum().getValue()) - 32) * 5)/9;

                BigDecimal bd_min = new BigDecimal(Float.toString(temp_min_cel));
                bd_min = bd_min.setScale(1, BigDecimal.ROUND_CEILING);

                if(saved_child_exists){
                    mWeatherRef.child("Saved Cities").child(city).child("temperature").setValue(bd_min.floatValue()+DEGREE_CELECIUS_SIGN_UNICODE);
                    mWeatherRef.child("Saved Cities").child(city).child("time").setValue(dateToSave);
                    Toast.makeText(CityWeatherActivity.this,"City Updated",Toast.LENGTH_SHORT).show();
                }else {

                    RequestParams params = new RequestParams(CURRENT_FORECAST_URL,"GET");

                    params.setCountry(country);
                    params.setCity(city);

                    params.setCity_key(city_key);
                    params.addParam("apikey",API_KEY);

                    new GetCityWeatherAsyncTask(CityWeatherActivity.this).execute(params);

                    }
                break;
            case R.id.action_set_current:

                String prev_key = myPrefs.getString(PREF_KEY_TAG,"");

                prefsEditor = myPrefs.edit();

                prefsEditor.clear().commit();
                prefsEditor.putString(PREF_CITY_TAG,city);
                prefsEditor.putString(PREF_COUNTRY_TAG,country);
                prefsEditor.putString(PREF_KEY_TAG,city_key);

                prefsEditor.commit();

                if (!prev_key.equals("")) {
                    Toast.makeText(CityWeatherActivity.this,"Current City Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CityWeatherActivity.this,"Current City Saved",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_settings:
                Intent intent = new Intent(CityWeatherActivity.this,PreferenceActivity.class);
                startActivityForResult(intent,1);

                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            String temperature;

            if(myPrefs.getString("temperatureUnit", "0").equals("0")){
                //Log.d("temperature",forecast.get().toString());
                //convert to C
                float temp_min_cel = ((Float.parseFloat(dailyForecasts.get(current).getMinimum().getValue()) - 32) * 5)/9;
                float temp_max_cel = ((Float.parseFloat(dailyForecasts.get(current).getMaximum().getValue()) - 32) * 5)/9;
                BigDecimal bd_min = new BigDecimal(Float.toString(temp_min_cel));
                bd_min = bd_min.setScale(1, BigDecimal.ROUND_CEILING);
                BigDecimal bd_max = new BigDecimal(Float.toString(temp_max_cel));
                bd_max = bd_max.setScale(1, BigDecimal.ROUND_CEILING);

                temperature = "Temperature " + bd_max.floatValue()+ "/" + bd_min.floatValue();

            }else{
                temperature = "Temperature " + dailyForecasts.get(current).getMaximum().getValue() + "/" + dailyForecasts.get(current).getMinimum().getValue();
            }
            tv_temp.setText(temperature);
        }
    }
}
