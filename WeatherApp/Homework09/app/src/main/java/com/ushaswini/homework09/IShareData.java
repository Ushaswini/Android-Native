package com.ushaswini.homework09;


/**
 * Vinnakota Venkata Ratna Ushaswini
 * IShareData
 * 03/04/2017
 */

public interface IShareData {

    void postLocation(CityDetails cityDetails);

    void updateProgressBar(boolean show);

    void postWeather(Weather weather);

    void updateFirebaseForFavorite(Weather weather);

    void postForecasts(FiveDayForecasts forecasts);

    void handleItemClick(int position);

    void handleLongItemClick(String city);
}
