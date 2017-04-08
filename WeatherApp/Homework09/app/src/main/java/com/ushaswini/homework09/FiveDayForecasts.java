package com.ushaswini.homework09;


import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * DailyForecasts
 * 04/04/2017
 */

public class FiveDayForecasts{

    String headline;

    String headline_mobileLink;

    public String getHeadline_mobileLink() {
        return headline_mobileLink;
    }

    public void setHeadline_mobileLink(String headline_mobileLink) {
        this.headline_mobileLink = headline_mobileLink;
    }

    ArrayList<DailyForecasts> dailyForecasts;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public ArrayList<DailyForecasts> getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(ArrayList<DailyForecasts> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    public FiveDayForecasts() {
    }

    @Override
    public String toString() {
        return "FiveDayForecasts{" +
                "text='" + headline + '\'' +
                ", dailyForecasts=" + dailyForecasts.toString() +
                '}';
    }
}


 class DailyForecasts {

    String date,mobile_link;

    Metric minimum, maximum;

    Icon day, night;

     public String getDate() {
         return date;
     }

     public void setDate(String date) {
         this.date = date;
     }

     public String getMobile_link() {
         return mobile_link;
     }

     public void setMobile_link(String mobile_link) {
         this.mobile_link = mobile_link;
     }

     public Metric getMinimum() {
         return minimum;
     }

     public void setMinimum(Metric minimum) {
         this.minimum = minimum;
     }

     public Metric getMaximum() {
         return maximum;
     }

     public void setMaximum(Metric maximum) {
         this.maximum = maximum;
     }

     public Icon getDay() {
         return day;
     }

     public void setDay(Icon day) {
         this.day = day;
     }

     public Icon getNight() {
         return night;
     }

     public void setNight(Icon night) {
         this.night = night;
     }

     public DailyForecasts() {
     }

     @Override
     public String toString() {
         return "DailyForecasts{" +
                 "date='" + date + '\'' +
                 ", mobile_link='" + mobile_link + '\'' +
                 ", minimum=" + minimum.toString() +
                 ", maximum=" + maximum.toString() +
                 ", day=" + day.toString() +
                 ", night=" + night.toString() +
                 '}';
     }
 }

class Icon{
    String icon, icon_phrase;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_phrase() {
        return icon_phrase;
    }

    public void setIcon_phrase(String icon_phrase) {
        this.icon_phrase = icon_phrase;
    }

    public Icon() {
    }

    @Override
    public String toString() {
        return "Icon{" +
                "icon='" + icon + '\'' +
                ", icon_phrase='" + icon_phrase + '\'' +
                '}';
    }
}

class Metric{

    public  String value, unit,unitType;
    public Metric() {
    }

    public  String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", unitType='" + unitType + '\'' +
                '}';
    }
}


