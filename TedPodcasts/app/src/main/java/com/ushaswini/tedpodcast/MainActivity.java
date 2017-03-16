package com.ushaswini.tedpodcast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IShareDataFromAsync,InterfaceUtil {

    static final String URL = "https://www.npr.org/rss/podcast.php?id=510298";

    RecyclerView rvPodcasts;
    ImageButton imageButton;
    SeekBar seekBar;
    ProgressDialog progressDialog;

    private MediaPlayer mediaPlayer;

    private final Handler handler = new Handler();

    List<Podcast> podcastsList;
    private int mediaFileLengthInMilliseconds;
    boolean isPlaying;
    boolean isVertical;

    PodcastsAdapterVertical adapterVertical;

    PodcastsAdapterGrid adapterGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TED Radio Hour Podcast");
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_ted);
        actionBar.setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgress(0);
        progressDialog.setMessage("Loading Episodes");
        progressDialog.setCancelable(false);
        progressDialog.show();

        rvPodcasts  = (RecyclerView) findViewById(R.id.rvPodcasts);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        mediaPlayer = new MediaPlayer();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("isPlaying",isPlaying+"");
                if(isPlaying){
                    mediaPlayer.pause();
                    imageButton.setImageResource(R.drawable.ic_play_arrow);
                    isPlaying = false;
                }else{
                    mediaPlayer.start();
                    imageButton.setImageResource(R.drawable.ic_pause);
                    isPlaying = true;
                }
                primarySeekbarProgressUpdater();

            }
        });

        seekBar.setMax(99);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.seekBar){
                    if(mediaPlayer.isPlaying()){
                        SeekBar sb = (SeekBar)v;
                        int playPositionInMilliSeconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                        mediaPlayer.seekTo(playPositionInMilliSeconds);
                    }
                }
                return false;
            }
        });

        new GetPodCastAsyncTask(MainActivity.this).execute(URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mediaPlayer){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        imageButton.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);

        if(isVertical){
            rvPodcasts.setAdapter(adapterGrid);
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            rvPodcasts.setLayoutManager(gridLayoutManager);
            isVertical = false;
        }else{
            rvPodcasts.setAdapter(adapterVertical);
            rvPodcasts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            isVertical = true;
        }
        return true;
    }

    private void primarySeekbarProgressUpdater(){

        int progress = (int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds ) * 100);
        seekBar.setProgress(progress);

        if(mediaPlayer.isPlaying()){
            Runnable notification = new Runnable() {
                @Override
                public void run() {
                   primarySeekbarProgressUpdater();
                }
            };
            handler.postDelayed(notification,0);
        }
    }

    @Override
    public void postPodcasts(ArrayList<Podcast> podcasts) {

        podcastsList = podcasts;

        progressDialog.hide();

        Log.d("podcasts before",podcasts.toString());

        Collections.sort(podcastsList,Podcast.PodcastDateComparator);
        //podcastsList = podcasts;

        Log.d("podcasts after",podcastsList.toString());

        adapterVertical = new PodcastsAdapterVertical(podcastsList,MainActivity.this,MainActivity.this);

        adapterGrid = new PodcastsAdapterGrid(podcastsList,MainActivity.this,MainActivity.this);

        rvPodcasts.setAdapter(adapterVertical);

        rvPodcasts.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        isVertical = true;
    }

    @Override
    public void playMp3(String Url) {

        imageButton.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageButton.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
            }
        });
        try {
            mediaPlayer.setDataSource(Url);
            mediaPlayer.prepare();
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                imageButton.setImageResource(R.drawable.ic_pause);
                isPlaying = true;
            }
            primarySeekbarProgressUpdater();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleItemClick(Podcast podcast) {

        if(isPlaying){
            imageButton.setImageResource(R.drawable.ic_play_arrow);
        }
        progressDialog.setMessage("Getting Podcast Details");
        progressDialog.show();

        if( null != mediaPlayer ){
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        Intent intent = new Intent(MainActivity.this,PlayActivity.class);
        intent.putExtra("DATA",podcast);

        progressDialog.hide();

        startActivity(intent);


    }
}
