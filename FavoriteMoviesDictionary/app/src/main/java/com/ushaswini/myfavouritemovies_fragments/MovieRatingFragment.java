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
 * {@link MovieRatingFragment.OnRatingFragmentActionListner} interface
 * to handle interaction events.
 * Use the {@link MovieRatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieRatingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRatingFragmentActionListner mListener;
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
    public MovieRatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieRatingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieRatingFragment newInstance(String param1, String param2) {
        MovieRatingFragment fragment = new MovieRatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_rating, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRatingFragmentActionListner) {
            mListener = (OnRatingFragmentActionListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Movies By Rating");

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

        if(movies != null& movies.size()>0){

            Collections.sort(movies, new Comparator<Movie>(){

                @Override
                public int compare(Movie o1, Movie o2) {
                    if(o1.getRating() > o2.getRating()){
                        return -1;
                    }else if(o1.getRating() < o2.getRating()){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });
            Movie movie_to_display = movies.get(0);
            setParams(movie_to_display);
        }
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
    public interface OnRatingFragmentActionListner {
        // TODO: Update argument type and name
        void returnFromFragment();
    }
}
