/*
* In class-06
* Vinnakota Venkata Ratna Ushaswini*/
package com.ushaswini.inclass06;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IShareData{

    static final String BASE_URL = "http://thegamesdb.net/api/";
    static final String GET_LIST_URL = BASE_URL + "GetGamesList.php";
    static final String GET_GAME_URL = BASE_URL + "GetGame.php";
    static final String METHOD_GET = "GET";
    static final String GAME_DETAILS_KEY = "GAME_DETAILS";
    static final String IMAGE_1 = "http://thegamesdb.net/banners/clearlogo/";
    static final String IMAGE_2 = ".png";


    Button btn_search;
    EditText eT_input;

    ListView listView;
    ArrayList<GameBasic>gameBasics;
    ProgressDialog progressDialog;
    int progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("The Games DB");
        setContentView(R.layout.activity_main);


        btn_search = (Button) findViewById(R.id.btn_Search);
        eT_input = (EditText) findViewById(R.id.eT_Input);
        listView = (ListView) findViewById(R.id.listViewGames);

        if(!isConnectedOnline()){
            Toast.makeText(MainActivity.this,"Not Connected",Toast.LENGTH_SHORT).show();
            finish();
        }

        //progressBar.setVisibility(View.GONE);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inp = eT_input.getText().toString();
                RequestParams params = new RequestParams(GET_LIST_URL,METHOD_GET);
                params.addParam("name",inp);
                //progressBar.setVisibility(View.VISIBLE);
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setProgress(0);
                progressDialog.setMessage("Getting Games");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new GetGamesAsyncTask(MainActivity.this).execute(params);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RequestParams params = new RequestParams(GET_GAME_URL,METHOD_GET);
                params.addParam("id",Integer.toString(gameBasics.get(position).getId()));

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setProgress(0);
                progressDialog.setMessage("Getting Game Details");
                progressDialog.setCancelable(false);

                new GetGameDetailsAsyncTask(MainActivity.this).execute(params);
                progressDialog.show();


            }
        });

    }

    public  boolean isConnectedOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(null != ni & ni.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void postBitmap(Bitmap image) {

    }

    @Override
    public void postGamesList(ArrayList<GameBasic> gameArrayList) {

        if(gameArrayList.size()>10){
            gameArrayList = new ArrayList<>(gameArrayList.subList(0,10));
        }
        gameBasics = gameArrayList;
        for(int i=0;i<gameArrayList.size();i++){
            String id = Integer.toString(gameArrayList.get(i).getId());

            gameArrayList.get(i).setImageUrl(IMAGE_1 + id +IMAGE_2);
        }
        GameBasicArrayAdapter arrayAdapter = new GameBasicArrayAdapter(MainActivity.this,R.layout.list_row_item,gameBasics);
        listView.setAdapter(arrayAdapter);
        progressDialog.hide();


    }

    @Override
    public void postGameDetails(GameDetails gameDetails) {
        progressDialog.hide();

        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra(GAME_DETAILS_KEY,gameDetails);
        startActivity(intent);
    }

    @Override
    public void postImageUrl(String url) {
        gameBasics.get(progress).setImageUrl(url);
        progress++;

        if(progress == gameBasics.size()){

            GameBasicArrayAdapter arrayAdapter = new GameBasicArrayAdapter(MainActivity.this,R.layout.list_row_item,gameBasics);
            listView.setAdapter(arrayAdapter);
        }


    }
}
