package com.ushaswini.tripplanner;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Objects;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * PlaceDetails
 * 28/04/2017
 */

public class PlaceDetails implements Serializable {
    String name, address;
            Double lat,lng;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public PlaceDetails() {
    }

    @Override
    public String toString() {
        return "PlaceDetails{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof PlaceDetails)) {
            return false;
        }
        PlaceDetails place = (PlaceDetails) obj;
        return name.equals(place.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
