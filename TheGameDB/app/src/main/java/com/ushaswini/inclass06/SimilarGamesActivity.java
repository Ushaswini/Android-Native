/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
package com.ushaswini.inclass06;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SimilarGamesActivity extends AppCompatActivity implements IShareData {

    TextView tv_Title;
    Button btn_finish;
    ListView listView;
    ProgressDialog progressDialog1;

    ArrayList<GameBasic>similarGames;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);

        setTitle("Similar Games");

        setContentView(R.layout.activity_similar_games);

        try {
            tv_Title = (TextView) findViewById(R.id.tv_similar_title);
            btn_finish = (Button) findViewById(R.id.btn_similar_finish);
            listView = (ListView) findViewById(R.id.listView_SimilarGames);

            if(getIntent().getExtras() != null){
                if(getIntent().getExtras().containsKey(MainActivity.GAME_DETAILS_KEY)){

                    similarGames = (ArrayList<GameBasic>) getIntent().getExtras().get(MainActivity.GAME_DETAILS_KEY);

                    if(getIntent().getExtras().containsKey(DetailsActivity.TITLE_KEY)){
                        title = tv_Title.getText()+ " "+(String) getIntent().getExtras().get(DetailsActivity.TITLE_KEY);
                        tv_Title.setText(title);
                    }
                    //GameBasicArrayAdapter arrayAdapter = new GameBasicArrayAdapter(this,R.layout.list_row_item,similarGames);

                   ArrayAdapter<GameBasic>arrayAdapter = new ArrayAdapter<GameBasic>(this,android.R.layout.simple_list_item_1,similarGames);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            RequestParams params = new RequestParams(MainActivity.GET_GAME_URL,MainActivity.METHOD_GET);
                            Log.d("ID",Integer.toString(similarGames.get(position).getId()));
                            params.addParam("id",Integer.toString(similarGames.get(position).getId()));

                            progressDialog1 = new ProgressDialog(SimilarGamesActivity.this);
                            progressDialog1.setProgress(0);
                            progressDialog1.setMessage("Getting Game Details");
                            progressDialog1.setCancelable(false);

                            new GetGameDetailsAsyncTask(SimilarGamesActivity.this).execute(params);
                            progressDialog1.show();
                        }
                    });

                }
            }
        }catch (Exception oExcep){
            Toast.makeText(SimilarGamesActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
        }

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent intent = new Intent(SimilarGamesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
                startActivity(intent);*/
            }
        });


    }

    @Override
    public void postBitmap(Bitmap image) {

    }

    @Override
    public void postGamesList(ArrayList<GameBasic> gameArrayList) {

    }

    @Override
    public void postGameDetails(GameDetails gameDetails) {
        progressDialog1.hide();
        Intent intent = new Intent(SimilarGamesActivity.this,DetailsSimilarActivity.class);
        intent.putExtra(MainActivity.GAME_DETAILS_KEY,gameDetails);
        startActivity(intent);
    }

    @Override
    public void postImageUrl(String url) {

    }
}
