package com.ushaswini.tripplanner;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
 * CustomTripAdapter
 * 18/04/2017
 */

public class CustomTripAdapter extends ArrayAdapter<TripDetails> {

    ArrayList<TripDetails> trips;

    Context mContext;

    int mResource;

    boolean isNew;

    String Organizer_id;


    public CustomTripAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<TripDetails> tripDetails, boolean isNew, String Organizer_id) {
        super(context, resource, tripDetails);
        this.trips = tripDetails;
        this.mContext = context;
        this.mResource = resource;
        this.isNew = isNew;
        this.Organizer_id = Organizer_id;

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
            holder.button = (Button) convertView.findViewById(R.id.btn_view);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
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
                    //TODO Show alert dialog
                }else{
                    Log.d("demo in adapter",Organizer_id);
                    Intent intent = new Intent(mContext,ViewTripActivity.class);
                    intent.putExtra("Organizer_id",Organizer_id);
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




class ViewHolder{

    TextView tv_title;
    TextView tv_location;
    ImageView imageView;
    Button button;
}
