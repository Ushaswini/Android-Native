/*
* Assignment : 5
* Vinnakota Venkata Ratna Ushaswini*/

package com.ushaswini.cnnnewsapp;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IShareDataFromAsync{

    Button btn_getNews, btn_finish;
    ImageButton im_first,im_previous,im_next,im_last;
    LinearLayout linear_news;
    ImageView imageView;
    ProgressBar progressBar;
    TextView tv_loading;

    int counter;
    ArrayList<NewsArticle>articlesReceived = new ArrayList<NewsArticle>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar mActionBar = getSupportActionBar();

        mActionBar.setTitle("CNN News");

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setLogo(R.mipmap.ic_launcher);
        mActionBar.setDisplayUseLogoEnabled(true);


        setContentView(R.layout.activity_main);

        btn_getNews = (Button)findViewById(R.id.btn_get_news);
        btn_finish = (Button)findViewById(R.id.buttonFinish);
        im_first = (ImageButton)findViewById(R.id.imageButtonFirst);
        im_previous = (ImageButton)findViewById(R.id.imageButtonPrevious);
        im_next = (ImageButton)findViewById(R.id.imageButtonNext);
        im_last = (ImageButton)findViewById(R.id.imageButtonLast);
        linear_news = (LinearLayout)findViewById(R.id.linear_news);
        imageView = (ImageView)findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_loading = (TextView) findViewById(R.id.tv_loading);

        im_first.setEnabled(false);
        im_previous.setEnabled(false);
        im_next.setEnabled(false);
        im_last.setEnabled(false);
        btn_finish.setEnabled(false);
        progressBar.setVisibility(View.GONE);
        tv_loading.setVisibility(View.GONE);

        btn_getNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetNewsAsyncTask(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_tech.rss");
            }
        });

        im_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter = counter - 1;
                    NewsArticle article = articlesReceived.get(counter);
                    setArticleParams(article);
                    new GetImageAsyncTask(MainActivity.this).execute(article.getUrlToImage());

                }else{
                    Toast.makeText(MainActivity.this,"Reached the first article. Change Direction",Toast.LENGTH_SHORT).show();
                }
            }
        });

        im_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                NewsArticle article = articlesReceived.get(counter);
                setArticleParams(article);
                new GetImageAsyncTask(MainActivity.this).execute(article.getUrlToImage());

            }
        });

        im_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter < articlesReceived.size() -1){
                    counter = counter+1;
                    NewsArticle article = articlesReceived.get(counter);
                    setArticleParams(article);
                    new GetImageAsyncTask(MainActivity.this).execute(article.getUrlToImage());

                }else{
                    Toast.makeText(MainActivity.this,"Reached the last article. Change Direction",Toast.LENGTH_SHORT).show();
                }
            }
        });

        im_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = articlesReceived.size()-1;
                NewsArticle article = articlesReceived.get(counter);
                setArticleParams(article);
                new GetImageAsyncTask(MainActivity.this).execute(article.getUrlToImage());

            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void postBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void postArticles(ArrayList<NewsArticle> articles) {

        articlesReceived = articles;
        NewsArticle articleFirst = articlesReceived.get(0);

        new GetImageAsyncTask(MainActivity.this).execute(articleFirst.getUrlToImage());

        setArticleParams(articleFirst);

        im_first.setEnabled(true);
        im_previous.setEnabled(true);
        im_next.setEnabled(true);
        im_last.setEnabled(true);
        btn_finish.setEnabled(true);
    }

    @Override
    public void updateProgressBar(boolean isComplete) {
        if(isComplete){
            progressBar.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            tv_loading.setVisibility(View.VISIBLE);
        }
    }

    public void setArticleParams(NewsArticle article){

        TextView Tv_title = new TextView(MainActivity.this);
        TextView Tv_published = new TextView(MainActivity.this);
        TextView Tv_description = new TextView(MainActivity.this);

        Tv_description.setText("Description: \n"+article.getDescription());
        Tv_description.setTypeface(null, Typeface.BOLD);


        Tv_title.setText(article.getTitle());
        Tv_title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,20);
        Tv_title.setLayoutParams(layoutParams);

        Tv_published.setText("Published on: "+article.getPubDateFormatted());
        Tv_published.setTypeface(null, Typeface.BOLD);

        layoutParams.setMargins(10,10,10,30);
        Tv_published.setLayoutParams(layoutParams);


        if(linear_news.getChildCount() >0){
            linear_news.removeAllViews();
        }

        linear_news.addView(Tv_title);
        linear_news.addView(Tv_published);
        linear_news.addView(Tv_description);
    }
}
