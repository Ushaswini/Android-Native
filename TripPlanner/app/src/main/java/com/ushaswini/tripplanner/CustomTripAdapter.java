package com.ushaswini.tripplanner;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * CustomTripAdapter
 * 18/04/2017
 */

public class CustomTripAdapter extends ArrayAdapter<TripDetails> {

    ArrayList<TripDetails> trips;

    Context mContext;

    int mResource;

    public CustomTripAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<TripDetails> tripDetails) {
        super(context, resource, tripDetails);
        this.trips = tripDetails;
        this.mContext = context;
        this.mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        final TripDetails trip = trips.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.list_row_item,parent,false);
            convertView = inflater.inflate(mResource,parent,false);

            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title_trip);
            holder.tv_location = (TextView)convertView.findViewById(R.id.tv_location);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        TextView title = holder.tv_title;
        TextView location = holder.tv_location;
        ImageView imageView = holder.imageView;
        title.setText(trip.getTitle());
        location.setText(trip.getLocation());
        Picasso.with(mContext).load(trip.getImageUrl()).into(imageView);




        return convertView;
    }

    @Override
    public int getCount() {
        return trips.size();
    }
}




class ViewHolder{

    TextView tv_title;
    TextView tv_location;
    ImageView imageView;
}
