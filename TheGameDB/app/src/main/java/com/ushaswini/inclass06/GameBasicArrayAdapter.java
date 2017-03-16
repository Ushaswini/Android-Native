/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.inclass06;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ushas on 20/02/2017.
 */

public class GameBasicArrayAdapter extends ArrayAdapter<GameBasic> {

    ArrayList<GameBasic> gamesList;
    int mResource;
    Context mContext;

    public GameBasicArrayAdapter(Context context, int resource, ArrayList<GameBasic> objects) {
        super(context, resource, objects);
        this.gamesList = objects;
        this.mContext = context;
        this.mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView)convertView.findViewById(R.id.tv_title);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar_image);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        ImageView imageView = holder.imageView;
        TextView textView = holder.textView;
        final ProgressBar pb = holder.progressBar;



        //imageView.setImageBitmap(gamesList.get(position).getImageUrl());
       Log.d("imageurl",gamesList.get(position).getImageUrl());
        if(gamesList.get(position).getImageUrl() != null){
            Picasso.with(mContext)
                    .load(gamesList.get(position).getImageUrl()).error(R.mipmap.logo_notavailable)
                    .into(imageView,new ImageLoadedCallback(pb){
                        public void onSuccess(){
                            if (pb != null)
                                pb.setVisibility(View.GONE);
                        }
                        public void onError()
                        {
                            pb.setVisibility(View.GONE);
                        }
                    });


            //Picasso.with(mContext).load(gamesList.get(position).getImageUrl()).into(imageView);
        }else{
            imageView.setImageResource(R.mipmap.logo_notavailable);
            //imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.logo_notavailable));
        }
        textView.setText(gamesList.get(position).toString());

        return convertView;
    }

    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar pb) {
            progressBar = pb;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }
}
