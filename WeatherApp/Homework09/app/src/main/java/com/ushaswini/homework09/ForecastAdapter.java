package com.ushaswini.homework09;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ushaswini.homework09.MainActivity.WEATHER_ICON_URL;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * ForecastAdapter
 * 04/04/2017
 */

public class ForecastAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ForecastViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView tv_date;


        public ForecastViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.im_status);
            tv_date = (TextView) itemView.findViewById(R.id.textView);

            itemView.setClickable(true);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Check if an item was deleted, but the user clicked it before the UI removed it
                Log.d("podcast in onClick",position + "");
                // We can access the data within the views
                iShareData.handleItemClick(position);
            }
        }
    }

    Context mContext;

    ArrayList<DailyForecasts> dailyForecasts;

    IShareData iShareData;

    public ForecastAdapter(Context mContext, ArrayList<DailyForecasts> dailyForecasts) {
        Log.d("Inside adapter",dailyForecasts.toString());

        this.mContext = mContext;
        this.dailyForecasts = dailyForecasts;
        this.iShareData = (IShareData) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view_forecast = inflater.inflate(R.layout.custom_forecast_row,parent,false);

        holder = new ForecastAdapter.ForecastViewHolder(view_forecast);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ForecastAdapter.ForecastViewHolder holderVer = (ForecastAdapter.ForecastViewHolder) holder;
        configureViewHolder(holderVer,position);
    }

    public void configureViewHolder(ForecastAdapter.ForecastViewHolder holder, int position) {

        final DailyForecasts forecast = dailyForecasts.get(position);
        Log.d("Inside adapter",forecast.toString());

        String icon = forecast.getDay().getIcon();

        if(Integer.valueOf(icon) < 10){
            icon = "0" + icon;

        }
        Picasso.with(mContext)
                .load(WEATHER_ICON_URL + icon + "-s.png")
                .into(holder.imageView);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = format.parse(forecast.getDate());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM ''yy");
            String dateStr = dateFormat.format(date).toString();
            Log.d("date",dateStr);
            holder.tv_date.setText(dateStr);


        } catch (ParseException e) {

            Log.d("error",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dailyForecasts.size();
    }
}
