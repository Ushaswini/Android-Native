package com.ushaswini.inclass06;
/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailsSimilarActivity extends AppCompatActivity implements IShareData{

    TextView tv_title;
    TextView tv_genre;
    TextView tv_publisher;
    TextView tv_overview;
    Button btn_finish;
    ProgressDialog progressDialog;
    ImageView imageView;
    ProgressBar progressBar;

    GameDetails gameDetails;
    ArrayList<Integer> similarGameIds;
    ArrayList<GameBasic> similarGames;
    int similarCount,progress;

    static final String TITLE_KEY = "Similar_Title";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_similar);

        try {
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_genre = (TextView) findViewById(R.id.tv_genre);
            tv_publisher = (TextView) findViewById(R.id.tv_publisher);
            tv_overview = (TextView) findViewById(R.id.tv_description);
            btn_finish = (Button) findViewById(R.id.btn_finish);
            imageView = (ImageView) findViewById(R.id.imageView);
            progressBar = (ProgressBar) findViewById(R.id.progressBar_image);

            tv_overview.setMovementMethod(new ScrollingMovementMethod());

            similarGames = new ArrayList<>();

            /*btn_playTrailer.setEnabled(false);
            btn_similarGames.setEnabled(false);*/

            progressDialog = new ProgressDialog(DetailsSimilarActivity.this);
            progressDialog.setProgress(0);
            progressDialog.setMessage("Getting Similar Game Details");
            progressDialog.setCancelable(false);


            if(getIntent().getExtras() != null){
                if (getIntent().getExtras().containsKey(MainActivity.GAME_DETAILS_KEY)) {

                    gameDetails = (GameDetails) getIntent().getExtras().get(MainActivity.GAME_DETAILS_KEY);
                    progressBar.setVisibility(View.VISIBLE);

                    if(gameDetails.getImageUrl() != null){
                        new GetImageAsyncTask(DetailsSimilarActivity.this).execute(gameDetails.getImageUrl());
                    }

                    similarGameIds = gameDetails.getSimilarGamesId();
                    similarCount = gameDetails.getSimilarCount();

                    tv_title.setText(gameDetails.getGameTitle());

                    String title = tv_genre.getText() + (gameDetails.getGenre() == null ? "": gameDetails.getGenre());
                    tv_genre.setText(title);

                    String publisher = tv_publisher.getText() + (gameDetails.getPublisher() == null ? "": gameDetails.getPublisher());
                    tv_publisher.setText(publisher);

                    tv_overview.setText(gameDetails.getOverview());



                }
            }
        }catch (Exception oExcep){
            Toast.makeText(DetailsSimilarActivity.this,"Some Error Occured.",Toast.LENGTH_SHORT).show();
            Log.d("Error",oExcep.toString());
        }

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void postBitmap(Bitmap image) {
        progressBar.setVisibility(View.GONE);
        imageView.setImageBitmap(image);
    }

    @Override
    public void postGamesList(ArrayList<GameBasic> gameArrayList) {

    }

    @Override
    public void postGameDetails(GameDetails gameDetails) {

    }

    @Override
    public void postImageUrl(String url) {

    }
}
