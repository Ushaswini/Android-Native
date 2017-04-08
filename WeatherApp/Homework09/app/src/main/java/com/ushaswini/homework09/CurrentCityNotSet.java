package com.ushaswini.homework09;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentCityNotSet.OnSetCurrentCityFragmentListener} interface
 * to handle interaction events.
 */
public class CurrentCityNotSet extends Fragment {

    private OnSetCurrentCityFragmentListener mListener;

    public CurrentCityNotSet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_city_not_set, container, false);
    }


    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onSetCityButtonClicked();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetCurrentCityFragmentListener) {
            mListener = (OnSetCurrentCityFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    Button btn_set_current_city;
    ProgressBar progressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        btn_set_current_city = (Button) getView().findViewById(R.id.btn_set_current_city);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

        btn_set_current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });



    }

    public  void setProgressBarVisibility(boolean show){

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnSetCurrentCityFragmentListener {

        void onSetCityButtonClicked();
    }
}
