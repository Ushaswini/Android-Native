package com.ushaswini.homework09;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Weather
 * 04/04/2017
 */
@IgnoreExtraProperties
public class Weather {


    String localObservationDateTime, weatherText,weatherIcon,city,country;

    boolean isFavorite;

    Metric metric_celcius;

    public Metric getMetric_celcius() {
        return metric_celcius;
    }

    public void setMetric_celcius(Metric metric_celcius) {
        this.metric_celcius = metric_celcius;
    }

    public Metric getMetric_fahren() {
        return metric_fahren;
    }

    public void setMetric_fahren(Metric metric_fahren) {
        this.metric_fahren = metric_fahren;
    }

    Metric metric_fahren;

    public String getLocalObservationDateTime() {
        return localObservationDateTime;
    }

    public void setLocalObservationDateTime(String localObservationDateTime) {
        this.localObservationDateTime = localObservationDateTime;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "localObservationDateTime='" + localObservationDateTime + '\'' +
                ", weatherText='" + weatherText + '\'' +
                ", weatherIcon='" + weatherIcon + '\'' +
                ", metric=" + metric_celcius.getValue() + metric_celcius.getUnit()+metric_celcius.getUnitType()+
                '}';
    }

    public Weather() {

    }
}
