package com.ushaswini.tripplanner;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * AdapterCustomTrip
 * 18/04/2017
 */

public class AdapterCustomTrip extends ArrayAdapter<TripDetails> {

    ArrayList<TripDetails> trips;

    Context mContext;

    int mResource;

    boolean isNew;



    public AdapterCustomTrip(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<TripDetails> tripDetails, boolean isNew) {
        super(context, resource, tripDetails);
        this.trips = tripDetails;
        this.mContext = context;
        this.mResource = resource;
        this.isNew = isNew;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder_Chat holder;
        final TripDetails trip = trips.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.list_row_item,parent,false);
            convertView = inflater.inflate(mResource,parent,false);

            holder = new ViewHolder_Chat();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title_trip);
            holder.tv_location = (TextView)convertView.findViewById(R.id.tv_location);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.button = (Button) convertView.findViewById(R.id.btn_view);

            convertView.setTag(holder);
        }

        holder = (ViewHolder_Chat) convertView.getTag();
        TextView title = holder.tv_title;
        TextView location = holder.tv_location;
        ImageView imageView = holder.imageView;
        Button btn = holder.button;

        title.setText(trip.getTitle());
        location.setText(trip.getLocation());
        Picasso.with(mContext).load(trip.getImageUrl()).into(imageView);

        if(isNew){
            btn.setText("JOIN");
        }else{
            btn.setText("VIEW");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNew){
                    Intent intent = new Intent(mContext,JoinTripActivity.class);
                    intent.putExtra("trip_id",trip.getTrip_id());
                    mContext.startActivity(intent);

                }else{
                    Intent intent = new Intent(mContext,ViewTripActivity.class);
                    intent.putExtra("trip_id",trip.getTrip_id());
                    mContext.startActivity(intent);
                }

            }
        });





        return convertView;
    }

    @Override
    public int getCount() {
        return trips.size();
    }
}




