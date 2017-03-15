package com.ushaswini.myfavouritemovies_fragments;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * Movie
 * 13/03/2017
 */

public class Movie implements Parcelable {

    private String name;
    private String description;
    private int genre;
    private int rating;
    private String year;
    private String idmb;

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getIdmb() {
        return idmb;
    }

    public int getGenre() {
        return genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setIdmb(String idmb) {
        this.idmb = idmb;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getYear() { return year; }

    public void setYear(String year) {
        this.year = year;
    }

    public Movie(String description, int genre, String idmb, String name, int rating, String year) {
        super();
        this.description = description;
        this.genre = genre;
        this.idmb = idmb;
        this.name = name;
        this.rating = rating;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", genre=" + genre +
                ", rating=" + rating +
                ", year='" + year + '\'' +
                ", idmb='" + idmb + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(genre);
        dest.writeInt(rating);
        dest.writeString(year);
        dest.writeString(idmb);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }


    };

    private Movie(Parcel in){
        this.name = in.readString();
        this.description = in.readString();
        this.genre = in.readInt();
        this.rating = in.readInt();
        this.year = in.readString();
        this.idmb = in.readString();
    }
}
