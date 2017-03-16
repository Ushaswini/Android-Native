package com.ushaswini.tedpodcast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * PodcastsAdapterGrid
 * 07/03/2017
 */

public class PodcastsAdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ViewHolderGrid extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView tv_title;
        ImageButton im_play;

        public ViewHolderGrid(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView_grid);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title_grid);
            im_play = (ImageButton) itemView.findViewById(R.id.im_play_grid);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Podcast podcast = mPodcasts.get(position);
                Log.d("podcast in onClick",podcast.toString());
                // We can access the data within the views
                interfaceUtil.handleItemClick(podcast);
            }
        }
    }

    private List<Podcast> mPodcasts;

    private Context mContext;

    private InterfaceUtil interfaceUtil;

    public PodcastsAdapterGrid(List<Podcast> mPodcasts, Context mContext, InterfaceUtil interfaceUtil) {
        this.mPodcasts = mPodcasts;
        this.mContext = mContext;
        this.interfaceUtil = interfaceUtil;
    }

    public Context getContext() {
        return mContext;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View viewGrid = inflater.inflate(R.layout.custom_row_grid,parent,false);
        holder = new ViewHolderGrid(viewGrid);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderGrid holderGrid = (ViewHolderGrid) holder;
        configureGridViewHolder(holderGrid,position);
    }

    public void configureGridViewHolder(ViewHolderGrid holder, int position) {

        final Podcast podcast = mPodcasts.get(position);

        TextView tv_title = holder.tv_title;
        String title = "";
        if(podcast.getEpisodeTitle().length() > 19){
           title = podcast.getEpisodeTitle().substring(0,19);
            title = title +"..";
        }else{
            title = podcast.getEpisodeTitle();
        }
        tv_title.setText(title);

        ImageView imageView = holder.imageView;
        Picasso.with(getContext())
                .load(podcast.getImageUrl())
                .placeholder(R.mipmap.ic_loading)
                .error(R.drawable.ic_error_outline)
                .into(imageView);

        ImageButton imageButton = holder.im_play;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceUtil.playMp3(podcast.getMp3Url());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPodcasts.size();
    }
}
