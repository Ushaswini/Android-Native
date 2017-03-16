package com.ushaswini.tedpodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * Abhishek Surya
 * PodcastsAdapter
 * 07/03/2017
 */

public class PodcastsAdapterVertical extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VERTICAL = 0, GRID = 1;

    public class ViewHolderVertical extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView tv_title;
        TextView tv_date;
        Button im_play;

        public ViewHolderVertical(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.im_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            im_play = (Button) itemView.findViewById(R.id.btn_play);

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

    static boolean isVertical = true;

    public PodcastsAdapterVertical(List<Podcast> mPodcasts, Context mContext, InterfaceUtil interfaceUtil) {
        this.mPodcasts = mPodcasts;
        this.mContext = mContext;
        this.interfaceUtil = interfaceUtil;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderVertical holderVer = (ViewHolderVertical) holder;
        configureVerticalViewHolder(holderVer,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View viewVertical = inflater.inflate(R.layout.custom_row_vertical,parent,false);
        holder = new ViewHolderVertical(viewVertical);

        return holder;
    }


    public void configureVerticalViewHolder(ViewHolderVertical holder, int position) {

        final Podcast podcast = mPodcasts.get(position);

        TextView tv_title = holder.tv_title;
        tv_title.setText(podcast.getEpisodeTitle());

        TextView tv_date = holder.tv_date;
        tv_date.setText("Posted on " + podcast.getPubDateStr());

        ImageView imageView = holder.imageView;
        Picasso.with(getContext())
                .load(podcast.getImageUrl())
                .placeholder(R.mipmap.ic_loading)
                .error(R.drawable.ic_error_outline)
                .into(imageView);

        Button imageButton = holder.im_play;
        imageButton.setAllCaps(false);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mp3 in first activity",podcast.getMp3Url());
                interfaceUtil.playMp3(podcast.getMp3Url());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPodcasts.size();
    }

}
