package com.example.dailynews.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dailynews.R;
import com.example.dailynews.adapter.BreakingNewsAdapter;
import com.example.dailynews.adapter.OfflineViewPagerAdapter;
import com.example.dailynews.adapter.ViewPagerAdapter;
import com.example.dailynews.apikey.NewsApiKey;
import com.example.dailynews.connectivity.OfflineNews;
import com.example.dailynews.fragment.NewsFragment;
import com.example.dailynews.jsondata.Articles;
import com.example.dailynews.jsondata.News;
import com.example.dailynews.jsondata.RetrofitUtilsInterface;
import com.example.dailynews.model.DBManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {
    String category;
    ViewPager2 viewPager;
    Retrofit retrofit;
    RetrofitUtilsInterface retrofitUtilsInterface;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        viewPager = findViewById(R.id.viewPager);
        Intent intent = getIntent();
        if(intent!=null)
        {
            category = intent.getStringExtra("category");
        }
        retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/").addConverterFactory(GsonConverterFactory.create()).build();
        retrofitUtilsInterface = retrofit.create(RetrofitUtilsInterface.class);
        getCategoryNews();
    }



    public void getCategoryNews()
    {
        if(!OfflineNews.isOffline_news()) {
            Call<News> data = retrofitUtilsInterface.getNewsCategoryWise("in", NewsApiKey.getApi_key(), category);
            data.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (!response.isSuccessful()) {
                        Log.d("retroFit", response.code() + "");
                        return;
                    }

                    News news = response.body();
                    ArrayList<Articles> articles = news.getArticles();

                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), articles,category);
                    viewPager.setAdapter(viewPagerAdapter);

                    new DBManager(getApplicationContext()).insertNews(articles, category);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Log.d("retroFit", t.getMessage());
                }
            });
        }
        else
        {
            Log.d("offlineNewsFragment","Offline News Downloaded");
            LinkedHashMap<Articles,String> newsData = new DBManager(getApplicationContext()).readNews(category);

            if(newsData.size()>0)
            {
                Log.d("offlineNewsFragment", "newsdata size " + newsData.size());
                ArrayList<LinkedHashMap<Articles, String>> nd = new ArrayList<>();

                for (Map.Entry<Articles, String> e : newsData.entrySet()) {
                    Articles a = e.getKey();
                    String i = e.getValue();
                    LinkedHashMap<Articles, String> n = new LinkedHashMap<>();
                    n.put(a, i);
                    nd.add(n);
                }
                OfflineViewPagerAdapter offlineViewPagerAdapter = new OfflineViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), nd, category);
                viewPager.setAdapter(offlineViewPagerAdapter);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewsActivity.this);
                builder.setMessage("No offline news available for this category !");
                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                             finish();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}