package com.ushaswini.homework09;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * SavedCitiesCustomAdapter
 * 04/04/2017
 */

public class SavedCitiesCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public class CityViewHolder  extends RecyclerView.ViewHolder {

        ImageButton ib_favorite;
        TextView tv_city;
        TextView tv_temp;
        TextView tv_updated;

        public CityViewHolder(View itemView) {
            super(itemView);

            ib_favorite = (ImageButton) itemView.findViewById(R.id.ib_fav);
            tv_city = (TextView) itemView.findViewById(R.id.tv_city);
            tv_temp = (TextView) itemView.findViewById(R.id.tv_updated);
            tv_updated = (TextView) itemView.findViewById(R.id.tv_temp);

            itemView.setLongClickable(true);
            itemView.setClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = getAdapterPosition();
                    Log.d("on long click",position+"");
                    iShareData.handleLongItemClick(cityWeatherDetails.get(position).getCity());

                    if(position != RecyclerView.NO_POSITION){
                    }
                    return true;
                }
            });


        }

    }

    ArrayList<Weather> cityWeatherDetails;

    Context mContext;

    IShareData iShareData;

    public SavedCitiesCustomAdapter(ArrayList<Weather> cityWeatherDetails, Context mContext) {
        this.cityWeatherDetails = cityWeatherDetails;
        this.mContext = mContext;
        this.iShareData = (IShareData)mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View viewVertical = inflater.inflate(R.layout.custom_row,parent,false);

        holder = new CityViewHolder(viewVertical);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CityViewHolder holderVer = (CityViewHolder) holder;

        holderVer.itemView.setLongClickable(true);
        holderVer.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("in long click",position+"");
                iShareData.handleLongItemClick(cityWeatherDetails.get(position).getCity());
                return true;
            }
        });

        configureViewHolder(holderVer,position);

    }

    public void configureViewHolder(CityViewHolder holder, int position) {

        final Weather cityDetails = cityWeatherDetails.get(position);

        Log.d("holder",holder.toString());


        final ImageButton ib_favorite = holder.ib_favorite;
        TextView tv_city = holder.tv_city;
        TextView tv_date = holder.tv_updated;
        TextView tv_temp = holder.tv_temp;

        tv_city.setText(cityDetails.getCity() + "," + cityDetails.getCountry());
        tv_temp.setText("Temperature : "+cityDetails.getMetric_celcius().getValue());

        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Log.d("time",cityDetails.getLocalObservationDateTime());
            Date date = dateFormat.parse(cityDetails.getLocalObservationDateTime());
            tv_date.setText("Last updated : "+prettyTime.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        final boolean isFavorite = cityDetails.isFavorite();
        if(isFavorite){

            ib_favorite.setImageResource(R.drawable.ic_star_full);

        }else {

            ib_favorite.setImageResource(R.drawable.ic_star);
        }
        ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFavorite){

                    cityDetails.setFavorite(false);
                    ib_favorite.setImageResource(R.drawable.ic_star);

                    iShareData.updateFirebaseForFavorite(cityDetails);
                }else{

                    cityDetails.setFavorite(true);
                    iShareData.updateFirebaseForFavorite(cityDetails);
                    ib_favorite.setImageResource(R.drawable.ic_star_full);

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return cityWeatherDetails.size();
    }
}
