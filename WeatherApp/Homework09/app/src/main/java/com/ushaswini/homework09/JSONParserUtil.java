package com.ushaswini.homework09;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * JSONParserUtil
 * 03/04/2017
 */

public class JSONParserUtil {

    public static class JSONParser{

        static CityDetails parseLocation(String input) throws JSONException {

            CityDetails cityDetails = new CityDetails();

            JSONArray array = new JSONArray(input);
            JSONObject object = array.getJSONObject(0);

            cityDetails.setKey(object.getString("Key"));
            cityDetails.setCity(object.getString("EnglishName"));
            cityDetails.setCountry(object.getJSONObject("Country").getString("ID"));

            return cityDetails;


        }

        static Weather parseWeather(String input) throws JSONException {
            Weather weather = new Weather();

            JSONArray array = new JSONArray(input);
            JSONObject object = array.getJSONObject(0);
            weather.setLocalObservationDateTime(object.getString("LocalObservationDateTime"));
            weather.setWeatherIcon(object.getString("WeatherIcon"));
            weather.setWeatherText(object.getString("WeatherText"));

            Metric metric_celcius =new Metric();
            Metric metric_fahren =new Metric();



            metric_celcius.setUnit(object.getJSONObject("Temperature").getJSONObject("Metric").getString("Unit"));
            metric_celcius.setUnitType(object.getJSONObject("Temperature").getJSONObject("Metric").getString("UnitType"));
            metric_celcius.setValue(object.getJSONObject("Temperature").getJSONObject("Metric").getString("Value"));

            metric_fahren.setUnit(object.getJSONObject("Temperature").getJSONObject("Imperial").getString("Unit"));
            metric_fahren.setUnitType(object.getJSONObject("Temperature").getJSONObject("Imperial").getString("UnitType"));
            metric_fahren.setValue(object.getJSONObject("Temperature").getJSONObject("Imperial").getString("Value"));

            weather.setMetric_celcius(metric_celcius);
            weather.setMetric_fahren(metric_fahren);


            return weather;
        }

        static FiveDayForecasts parseForecasts(String input) throws JSONException {


            FiveDayForecasts forecasts = new FiveDayForecasts();
            JSONObject object = new JSONObject(input);
            forecasts.setHeadline(object.getJSONObject("Headline").getString("Text"));

            forecasts.setHeadline_mobileLink(object.getJSONObject("Headline").getString("MobileLink"));

            JSONArray array = object.getJSONArray("DailyForecasts");
            ArrayList<DailyForecasts> dailyForecasts = new ArrayList<>();

            for(int i=0;i<array.length();i++){
                JSONObject jsonObject = array.getJSONObject(i);

                DailyForecasts dailyForecast = new DailyForecasts();

                Icon icon_day = new Icon();
                Icon icon_night = new Icon();

                Metric metric_min = new Metric();
                Metric metric_max = new Metric();

                metric_max.setUnit(jsonObject.getJSONObject("Temperature").getJSONObject("Maximum").getString("Unit"));
                metric_max.setUnitType(jsonObject.getJSONObject("Temperature").getJSONObject("Maximum").getString("UnitType"));
                metric_max.setValue(jsonObject.getJSONObject("Temperature").getJSONObject("Maximum").getString("Value"));

                metric_min.setUnit(jsonObject.getJSONObject("Temperature").getJSONObject("Minimum").getString("Unit"));
                metric_min.setUnitType(jsonObject.getJSONObject("Temperature").getJSONObject("Minimum").getString("UnitType"));
                metric_min.setValue(jsonObject.getJSONObject("Temperature").getJSONObject("Minimum").getString("Value"));

                icon_day.setIcon(jsonObject.getJSONObject("Day").getString("Icon"));
                icon_day.setIcon_phrase(jsonObject.getJSONObject("Day").getString("IconPhrase"));

                icon_night.setIcon(jsonObject.getJSONObject("Night").getString("Icon"));
                icon_night.setIcon_phrase(jsonObject.getJSONObject("Night").getString("IconPhrase"));

                dailyForecast.setDate(jsonObject.getString("Date"));
                dailyForecast.setDay(icon_day);
                dailyForecast.setNight(icon_night);
                dailyForecast.setMaximum(metric_max);
                dailyForecast.setMinimum(metric_min);
                dailyForecast.setMobile_link(jsonObject.getString("MobileLink"));

                dailyForecasts.add(dailyForecast);

            }

            forecasts.setDailyForecasts(dailyForecasts);

            return  forecasts;
        }
    }
}
