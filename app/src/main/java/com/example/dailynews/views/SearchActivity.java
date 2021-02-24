package com.example.dailynews.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dailynews.R;
import com.example.dailynews.adapter.BreakingNewsAdapter;
import com.example.dailynews.adapter.SearchAdapter;
import com.example.dailynews.apikey.NewsApiKey;
import com.example.dailynews.jsondata.Articles;
import com.example.dailynews.jsondata.News;
import com.example.dailynews.jsondata.RetrofitUtilsInterface;
import com.example.dailynews.listener.RecyclerClickListener;
import com.example.dailynews.model.DBManager;
import com.example.dailynews.webview.WebViewActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements RecyclerClickListener {
    SearchView searchView;
    RecyclerView recyclerView;
    Retrofit retrofit;
    RetrofitUtilsInterface retrofitUtilsInterface;
    ArrayList<Articles> articles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/").addConverterFactory(GsonConverterFactory.create()).build();
        retrofitUtilsInterface = retrofit.create(RetrofitUtilsInterface.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchNews(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchNews(s);
                return true;
            }
        });
    }

    public void searchNews(String q)
    {
        Call<News> data =  retrofitUtilsInterface.searchNews(NewsApiKey.getApi_key(),q);
        data.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (!response.isSuccessful()) {
                    Log.d("retroFit", response.code() + "");
                    return;
                }

                News news = response.body();
                articles = news.getArticles();
                SearchAdapter searchAdapter = new SearchAdapter(articles,getApplicationContext());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
                gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);
                searchAdapter.setRecyclerClickListener(SearchActivity.this);
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d("retroFit", t.getMessage());
            }
        });

    }

    @Override
    public void onClicked(int position)
    {
           Articles news = articles.get(position);
           String link = news.getUrl();
           Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
           intent.putExtra("link",link);
           startActivity(intent);
    }
}