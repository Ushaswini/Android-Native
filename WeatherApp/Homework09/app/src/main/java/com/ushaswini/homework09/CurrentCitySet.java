package com.ushaswini.homework09;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ushaswini.homework09.MainActivity.DEGREE_CELECIUS_SIGN_UNICODE;
import static com.ushaswini.homework09.MainActivity.DEGREE_FAHRENHEIT_SIGN_UNICODE;
import static com.ushaswini.homework09.MainActivity.WEATHER_ICON_URL;


public class CurrentCitySet extends Fragment {


    public CurrentCitySet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_city_set, container, false);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void postCityWeatherDetails(Weather weather){

        tv_city.setText(weather.getCity() + "," + weather.getCountry());
        //Log.d("weathertext",weather.getWeatherText());
        tv_status.setText(weather.getWeatherText());
        //Log.d("pref stored",myPrefs.getString("temperatureUnit", "0").equals("0")+"");
        if(myPrefs.getString("temperatureUnit", "0").equals("0")){
            //Log.d("temperature",weather.getMetric_celcius().toString());
            tv_temp.setText("Temperature: " + weather.getMetric_celcius().getValue()+DEGREE_CELECIUS_SIGN_UNICODE);
        }else{
            tv_temp.setText("Temperature: " + weather.getMetric_fahren().getValue()+DEGREE_FAHRENHEIT_SIGN_UNICODE);
        }

        tv_time.setText(weather.getLocalObservationDateTime());


        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            PrettyTime prettyTime = new PrettyTime();
            Date date = format.parse(weather.getLocalObservationDateTime());
            tv_time.setText("Updated "+prettyTime.format(date));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        String icon = weather.getWeatherIcon();

        if(Integer.valueOf(icon) < 10){
            icon = "0" + icon;

        }

        Picasso.with(getContext())
                .load(WEATHER_ICON_URL + icon + "-s.png")
                .into(imageView);
    }

    TextView tv_city;
    TextView tv_status;
    TextView tv_temp;
    TextView tv_time;
    ImageView imageView;
    SharedPreferences myPrefs;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_city = (TextView) getView().findViewById(R.id.tv_city);
        tv_status = (TextView) getView().findViewById(R.id.tv_status);
        tv_temp = (TextView) getView().findViewById(R.id.tv_updated);
        tv_time = (TextView) getView().findViewById(R.id.tv_time);
        imageView = (ImageView) getView().findViewById(R.id.imageView);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
