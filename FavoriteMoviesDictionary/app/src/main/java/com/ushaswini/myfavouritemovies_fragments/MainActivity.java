package com.ushaswini.myfavouritemovies_fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
                                    MainFragment.OnMainFragmentActionListner,
                                    MovieAddFragment.OnAddFragmentActionListner,
                                    MovieEditFragment.OnEditFragmentActionListner,
                                    MovieRatingFragment.OnRatingFragmentActionListner,
                                    MovieYearFragment.OnYearFragmentActionListner{

    public static final String MAIN_TAG = "Main_Fragment";
    public static final String ADD_TAG = "Add_Fragment";
    public static final String EDIT_TAG = "Edit_Fragment";
    public static final String YEAR_TAG = "Year_Fragment";
    public static final String RATING_TAG = "Rating_Fragment";

    private ArrayList<Movie> movies ;
    int movie_to_remove_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.linear_container,new MainFragment(),MAIN_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void addButtonClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linear_container,new MovieAddFragment(),ADD_TAG)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

    }

    @Override
    public void editButtonClicked() {
        try
        {
            if(movies.size() >0){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ArrayList<String> names = new ArrayList<String>() ;

                for ( Movie movie : movies ) {
                    names.add(movie.getName());
                }
                CharSequence[] namesArray = names.toArray(new CharSequence[names.size()]);

                builder.setTitle("Pick a movie")
                        .setItems(namesArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.linear_container,new MovieEditFragment(),EDIT_TAG)
                                        .addToBackStack(null)
                                        .commit();
                                getSupportFragmentManager().executePendingTransactions();
                                MovieEditFragment f = (MovieEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
                                movie_to_remove_index = which;
                                f.movieToEdit(movies.get(which));
                            }
                        });
                final AlertDialog alertDialog  = builder.create();
                alertDialog.show();
            }else{
                Toast.makeText(MainActivity.this,"No movies to edit",Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception oExcep){
            Toast.makeText(MainActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void deleteButtonClicked() {
        try
        {
            if(movies.size()>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ArrayList<String> names = new ArrayList<String>() ;

                for ( Movie movie : movies ) {
                    names.add(movie.getName());
                }
                CharSequence[] namesArray = names.toArray(new CharSequence[names.size()]);

                builder.setTitle("Pick a movie")
                        .setItems(namesArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String movie_name = movies.get(which).getName();
                                movies.remove(which);
                                Log.d("Delete", Integer.toString(which));
                                Toast.makeText(MainActivity.this,"Movie "+ movie_name+" Deleted",Toast.LENGTH_SHORT).show();

                            }
                        });
                final AlertDialog alertDialog  = builder.create();
                alertDialog.show();
            }else{
                Toast.makeText(MainActivity.this,"No movies to delete",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception oExcep)
        {
            Toast.makeText(MainActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void listByYearButtonClicked() {
        if(movies.size()>0){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.linear_container,new MovieYearFragment(),YEAR_TAG)
                    .addToBackStack(null)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
            MovieYearFragment f = (MovieYearFragment) getSupportFragmentManager().findFragmentByTag(YEAR_TAG);
            f.moviesToShow(movies);
        }else{
            Toast.makeText(MainActivity.this,"No movies to show",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void listByRatingButtonClicked() {
        if(movies.size() >0){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.linear_container,new MovieRatingFragment(),RATING_TAG)
                    .addToBackStack(null)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();

            MovieRatingFragment f = (MovieRatingFragment) getSupportFragmentManager().findFragmentByTag(RATING_TAG);
            f.moviesToShow(movies);
        }else{
            Toast.makeText(MainActivity.this,"No movies to show",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void addMovie(Movie movie) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linear_container,new MainFragment(),MAIN_TAG)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        Log.d("movie",movie.toString());
        movies.add(movie);

    }

    @Override
    public void editMovie(Movie movie) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linear_container,new MainFragment(),MAIN_TAG)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        movies.add(movie_to_remove_index,movie);
        movies.remove(movie_to_remove_index+1);
    }

    @Override
    public void returnFromFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linear_container,new MainFragment(),MAIN_TAG)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

    }
}
