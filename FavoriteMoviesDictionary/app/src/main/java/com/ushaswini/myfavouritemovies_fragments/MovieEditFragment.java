package com.ushaswini.myfavouritemovies_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieEditFragment.OnEditFragmentActionListner} interface
 * to handle interaction events.
 */
public class MovieEditFragment extends Fragment {

    private OnEditFragmentActionListner mListener;

    public MovieEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_edit, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditFragmentActionListner) {
            mListener = (OnEditFragmentActionListner) context;
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
    EditText Et_name;
    EditText Et_description;
    Spinner Sp_genre;
    SeekBar Sb_rating;
    EditText Et_year;
    EditText Et_idmb;
    Button Btn_edit;
    TextView Tv_rating_numeric;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Edit Movie");

        Et_name = (EditText)getView().findViewById(R.id.tv_Name_value);
        Et_description = (EditText)getView().findViewById(R.id.tv_description_value);
        Sp_genre = (Spinner)getView().findViewById(R.id.spinner_genre);
        Sb_rating = (SeekBar)getView().findViewById(R.id.seekBar_rating);
        Et_year = (EditText)getView().findViewById(R.id.tv_year_value);
        Et_idmb = (EditText)getView().findViewById(R.id.tv_idmb_value);
        Btn_edit = (Button)getView().findViewById(R.id.btn_editMovie);
        Tv_rating_numeric = (TextView)getView().findViewById(R.id.tV_Rating_numeric);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.genre_collection, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Sp_genre.setAdapter(adapter);
        Sb_rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Tv_rating_numeric.setText(Integer.toString(progress));
                //Log.d("progress",Integer.toString(rating_progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String name = Et_name.getText().toString();
                    if(name.length() > 50){
                        Toast.makeText(getContext(),"Only maximum of 50 characters are allowed for name field",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String description = Et_description.getText().toString();
                        if(description.length() > 1000){
                            Toast.makeText(getContext(),"Only maximum of 1000 characters are allowed for desciption field",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String year = Et_year.getText().toString();
                            String idmb = Et_idmb.getText().toString();
                            //Log.d("Output",name+description+year+idmb+Integer.toString(item_selected)+Integer.toString(rating_progress));

                            int item_selected = Sp_genre.getSelectedItemPosition();
                            int rating_progress = Sb_rating.getProgress();
                            Movie movie_to_edit = new Movie(description,item_selected,idmb,name,rating_progress,year);

                            mListener.editMovie(movie_to_edit);
                        }
                    }
                }
                catch(Exception oExcep)
                {
                    Toast.makeText(getContext(),"Invalid Inputs",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void movieToEdit(Movie movie){
        if(movie != null){
            Et_name.setText(movie.getName());
            Et_description.setText(movie.getDescription());
            Log.d("genre",Integer.toString(movie.getGenre()));
            Sp_genre.setSelection(movie.getGenre(),false);
            Sb_rating.setProgress(movie.getRating());
            Et_year.setText(movie.getYear());
            Et_idmb.setText(movie.getIdmb());
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
    public interface OnEditFragmentActionListner {
        // TODO: Update argument type and name
        void editMovie(Movie movie);
    }
}
