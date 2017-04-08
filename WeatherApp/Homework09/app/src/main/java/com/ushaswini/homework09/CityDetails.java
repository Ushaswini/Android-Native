package com.ushaswini.homework09;


/**
 * Vinnakota Venkata Ratna Ushaswini
 * CityDetails
 * 03/04/2017
 */

public class CityDetails {

    String city, key,country, temperature,time;

    boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public CityDetails() {
    }

    @Override
    public String toString() {
        return "CityDetails{" +
                "city='" + city + '\'' +
                ", key='" + key + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", time='" + time + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
