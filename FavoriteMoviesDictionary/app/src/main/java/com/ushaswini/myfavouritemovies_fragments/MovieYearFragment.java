package com.ushaswini.myfavouritemovies_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieYearFragment.OnYearFragmentActionListner} interface
 * to handle interaction events.
 */
public class MovieYearFragment extends Fragment {

    private OnYearFragmentActionListner mListener;
    TextView Tv_name;
    TextView Tv_description;
    TextView Tv_genre;
    TextView Tv_rating;
    TextView Tv_year;
    TextView Tv_idmb;

    Button Btn_finish;
    ImageButton Btn_first;
    ImageButton Btn_previous;
    ImageButton Btn_next;
    ImageButton Btn_last;

    int counter;


    ArrayList<Movie> movies;
    public MovieYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list_year, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnYearFragmentActionListner) {
            mListener = (OnYearFragmentActionListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void setParams(Movie movie_to_display){

        Tv_name.setText(movie_to_display.getName());
        Tv_description.setText(movie_to_display.getDescription());
        Tv_genre.setText(getResources().getStringArray(R.array.genre_collection)[movie_to_display.getGenre()]);
        String text = Integer.toString(movie_to_display.getRating()) + " / 5";
        Tv_rating.setText(text);
        Tv_year.setText(movie_to_display.getYear());
        Tv_idmb.setText(movie_to_display.getIdmb());
    }

    public void moviesToShow(ArrayList<Movie> moviesArray){
        movies = moviesArray;
        if(movies != null & movies.size()>0){

            Collections.sort(movies, new Comparator<Movie>(){

                @Override
                public int compare(Movie o1, Movie o2) {
                    if(Integer.parseInt(o1.getYear()) > Integer.parseInt(o2.getYear())){
                        return 1;
                    }else if(Integer.parseInt(o1.getYear()) < Integer.parseInt(o2.getYear())){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });

            Movie movie_to_display = movies.get(0);
            setParams(movie_to_display);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Movies By Year");

        Tv_name = (TextView)getView().findViewById(R.id.tv_Name_value);
        Tv_description = (TextView)getView().findViewById(R.id.tv_description_value);
        Tv_genre = (TextView)getView().findViewById(R.id.tv_genre_value);
        Tv_rating = (TextView)getView().findViewById(R.id.tv_rating_value);
        Tv_year = (TextView)getView().findViewById(R.id.tv_year_value);
        Tv_idmb = (TextView)getView().findViewById(R.id.tv_idmb_value);

        Btn_finish = (Button)getView().findViewById(R.id.button_finsih);
        Btn_first = (ImageButton)getView().findViewById(R.id.imageButton_first);
        Btn_previous = (ImageButton)getView().findViewById(R.id.imageButton_previous);
        Btn_next = (ImageButton)getView().findViewById(R.id.imageButton_next);
        Btn_last = (ImageButton)getView().findViewById(R.id.imageButton_last);

        Btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.returnFromFragment();
            }
        });

        Btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                Movie movie_to_display = movies.get(counter);
                setParams(movie_to_display);
            }
        });

        Btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter > 0){
                    counter = counter-1;

                    Log.d("Counter",Integer.toString(counter));
                    setParams(movies.get(counter));
                }
                else {
                    Toast.makeText(getContext(),"Reached the first movie. Change Direction",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = movies.size()-1;
                Movie movie_to_display = movies.get(counter);
                setParams(movie_to_display);
            }
        });

        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(counter < movies.size()-1){
                    counter = counter + 1;

                    Log.d("Counter",Integer.toString(counter));
                    setParams(movies.get(counter));
                }
                else
                {
                    Toast.makeText(getContext(),"Reached the last movie. Change Direction",Toast.LENGTH_SHORT).show();
                }
            }
        });


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
    public interface OnYearFragmentActionListner {
        // TODO: Update argument type and name
        void returnFromFragment();
    }
}
