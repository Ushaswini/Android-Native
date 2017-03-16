package com.ushaswini.tedpodcast;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayActivity extends AppCompatActivity {

    TextView tv_title;
    TextView tv_description;
    TextView tv_pub_date;
    TextView tv_duration;
    ImageView imageView;
    ImageButton imageButton;
    SeekBar seekBar;
    Podcast podcast;
    ProgressDialog progressDialog;

    MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    boolean isPlaying;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        tv_title = (TextView) findViewById(R.id.tv_title_play);
        tv_description = (TextView) findViewById(R.id.tv_description_play);
        tv_pub_date = (TextView) findViewById(R.id.tv_pub_date_play);
        tv_duration = (TextView) findViewById(R.id.tv_duration_play);
        imageView = (ImageView) findViewById(R.id.imageView_play);
        imageButton = (ImageButton) findViewById(R.id.im_mp3_play);
        seekBar = (SeekBar) findViewById(R.id.seekBar_play);

        progressDialog = new ProgressDialog(PlayActivity.this);
        progressDialog.setProgress(0);
        progressDialog.setMessage("Loading Details");
        progressDialog.setCancelable(false);
        progressDialog.show();




    }

    private void primarySeekbarProgressUpdater(){

        if(null != mediaPlayer){
            if(mediaPlayer.isPlaying()){

                int progress = (int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds ) * 100);
                seekBar.setProgress(progress);
                Runnable notification = new Runnable() {
                    @Override
                    public void run() {
                        primarySeekbarProgressUpdater();
                    }
                };
                handler.postDelayed(notification,0);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("DATA")){
                podcast = (Podcast) getIntent().getExtras().get("DATA");

                tv_title.setText(podcast.getEpisodeTitle());
                tv_description.setText("Description:  " + podcast.getDescription());
                tv_pub_date.setText("Publication Date:  " + podcast.getPubDateStr());
                Picasso.with(this)
                        .load(podcast.getImageUrl())
                        .placeholder(R.mipmap.ic_loading)
                        .error(R.drawable.ic_error_outline)
                        .into(imageView);
            }
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
            Log.d("mp3 received", podcast.getMp3Url());

            mediaPlayer.setDataSource(podcast.getMp3Url());
            mediaPlayer.prepare();
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            int durationInMin = mediaFileLengthInMilliseconds / 60000;
            tv_duration.setText("Duration: " + durationInMin + " minutes");

        } catch (IOException e) {
            e.printStackTrace();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        progressDialog.hide();
    }

    @Override
    protected void onPause() {
        Log.d("In pause","working");

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
}
