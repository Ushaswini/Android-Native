package com.ushaswini.tripplanner;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * AdapterPlaces
 * 28/04/2017
 */

public class AdapterPlaces extends ArrayAdapter<PlaceDetails> {

    ArrayList<PlaceDetails> mData;
    IShareData iShareData;
    Context mContext;
    int mResource;
    boolean showActionButtons;



    public AdapterPlaces(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PlaceDetails> objects,boolean showActionButtons) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mData = objects;
        this.showActionButtons = showActionButtons;

        this.iShareData = (AdapterPlaces.IShareData) context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final PlaceDetails place = mData.get(position);

        ViewHolder_Trip holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);

            holder = new ViewHolder_Trip();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_place);
            holder.im_edit = (ImageButton) convertView.findViewById(R.id.im_edit);
            holder.im_delete = (ImageButton) convertView.findViewById(R.id.im_delete);

            convertView.setTag(holder);
        }

        holder = (ViewHolder_Trip) convertView.getTag();
        TextView title = holder.tv_name;
        ImageButton im_edit = holder.im_edit;
        ImageButton im_delete = holder.im_delete;

        if(showActionButtons){
            im_delete.setVisibility(View.VISIBLE);
            im_edit.setVisibility(View.VISIBLE);
        }else{
            im_delete.setVisibility(View.INVISIBLE);
            im_edit.setVisibility(View.INVISIBLE);
        }

        title.setText(place.getAddress());

        im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iShareData.editPlace(position);
            }
        });

        im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iShareData.deletePlace(position);
            }
        });

        return convertView;

    }

    interface  IShareData{
        void editPlace(int position);

        void deletePlace(int position);

    }

    class ViewHolder_Trip{
        TextView tv_name;
        ImageButton im_edit;
        ImageButton im_delete;
    }
}
