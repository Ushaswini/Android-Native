package com.ushaswini.itunestoppaidapps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/*
* AppsArrayAdapter.jave
        * Vinnakota Venkata Ratna Ushaswini
        * Abhishekh Surya*/

public class AppsArrayAdapter extends ArrayAdapter<App> {

    ArrayList<App> appList;
    int mResource;
    Context mContext;

    public AppsArrayAdapter(Context context, int resource, ArrayList<App> appList) {
        super(context, resource);
        this.appList = appList;
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        try {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource,parent,false);

                holder = new ViewHolder();
                holder.image_view = (ImageView) convertView.findViewById(R.id.image_view);
                holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
                holder.ib_favorite = (ImageButton) convertView.findViewById(R.id.ib_favorite);

                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();

            ImageView image_view = holder.image_view;
            TextView tv_description = holder.tv_description;
            final ImageView image_favorite = holder.ib_favorite;
            final App app = appList.get(position);

            //Get saved preferences
            final SharedPreferences myPrefs = getContext().getSharedPreferences("com.ushaswini.itunestoppaidapps", MODE_PRIVATE);
            final SharedPreferences.Editor prefsEditor = myPrefs.edit();
            final Set<String> nullSet = new ArraySet<String>();
            final Set<String> set = myPrefs.getStringSet("app_favorites",nullSet);

            if(set.contains(app.getAppName()))
                image_favorite.setImageBitmap(BitmapFactory.decodeResource(convertView.getResources(),
                        R.drawable.black_star));
            else
                image_favorite.setImageBitmap(BitmapFactory.decodeResource(convertView.getResources(),
                        R.drawable.white_star));

            //Reset values
            image_view.setImageBitmap(null);
            tv_description.setText("");

            //Load image
            Picasso.with(mContext)
                    .load(app.getThumbUrl())
                    .placeholder(R.mipmap.user_placeholder)
                    .error(R.mipmap.error)
                    .into(image_view);

            tv_description.setText(app.toString());

            image_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //If already favorite, remove it from favorites list
                    if(set.contains(app.getAppName())) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Remove from Favorites")
                                .setMessage("Are you sure that you want to remove this App from favorites?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        set.remove(app.getAppName());
                                        image_favorite.setImageResource(R.drawable.white_star);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();

                    }else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Add to Favorites")
                                    .setMessage("Are you sure that you want to add this App to favorites?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            set.add(app.getAppName());
                                            image_favorite.setImageResource(R.drawable.black_star);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .show();
                        }
                        prefsEditor.putStringSet("app_favorites",set);
                        prefsEditor.commit();
                }
            });
        }catch (Exception oExcep){
            Log.d("excep",oExcep.getMessage());
        }
        return convertView;
    }
}
