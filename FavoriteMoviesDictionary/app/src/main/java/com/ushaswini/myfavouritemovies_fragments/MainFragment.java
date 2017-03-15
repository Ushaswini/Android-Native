package com.ushaswini.myfavouritemovies_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnMainFragmentActionListner} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment {

    private OnMainFragmentActionListner mListener;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentActionListner) {
            mListener = (OnMainFragmentActionListner) context;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("My Favorite Movies");

        Button btn_add = (Button) getView().findViewById(R.id.btn_add);
        Button btn_edit = (Button)getView().findViewById(R.id.btn_edit);
        Button btn_delete = (Button)getView().findViewById(R.id.btn_delete);
        Button btn_list_by_rating = (Button)getView().findViewById(R.id.btn_show_rating);
        Button btn_list_by_year = (Button)getView().findViewById(R.id.btn_show_year);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addButtonClicked();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editButtonClicked();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteButtonClicked();
            }
        });

        btn_list_by_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.listByRatingButtonClicked();
            }
        });

        btn_list_by_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.listByYearButtonClicked();
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
    public interface OnMainFragmentActionListner {
        // TODO: Update argument type and name
        void addButtonClicked();
        void editButtonClicked();
        void deleteButtonClicked();
        void listByYearButtonClicked();
        void listByRatingButtonClicked();

    }
}
