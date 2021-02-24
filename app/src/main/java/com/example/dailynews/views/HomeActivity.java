package com.example.dailynews.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.dailynews.adapter.OfflineBreakingNewsAdapter;
import com.example.dailynews.broadcastreceiver.InternetBroadcastReceiver;
import com.example.dailynews.connectivity.InternetConnectivity;
import com.example.dailynews.connectivity.OfflineNews;
import com.example.dailynews.listener.RecyclerClickListener;
import com.example.dailynews.notifications.NotificationJobScheduler;
import com.example.dailynews.pojo.Category;
import com.example.dailynews.R;
import com.example.dailynews.adapter.BreakingNewsAdapter;
import com.example.dailynews.adapter.CategoryAdapter;
import com.example.dailynews.apikey.NewsApiKey;
import com.example.dailynews.jsondata.Articles;
import com.example.dailynews.jsondata.News;
import com.example.dailynews.jsondata.RetrofitUtilsInterface;
import com.example.dailynews.model.DBManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements RecyclerClickListener {
    RetrofitUtilsInterface interf;
    RecyclerView recyclerView,catrecycler;
    BreakingNewsAdapter adapter;
    LinkedList<Category> categories;
    CategoryAdapter categoryAdapter;
    //create these 2 variables
    AlertDialog dialog = null;
    InternetBroadcastReceiver internetBroadcastReceiver;
    ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        ComponentName componentName = new ComponentName(HomeActivity.this, NotificationJobScheduler.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            JobInfo info = new JobInfo.Builder(123,componentName).setRequiresStorageNotLow(true).build();

            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(info);
        }

        HashMap<Articles,String> data = new DBManager(getApplicationContext()).readNews("health");

        Set<Map.Entry<Articles,String>> set = data.entrySet();

        for(Map.Entry<Articles,String> e:set)
        {
            Log.d("dbData","Title =  "+e.getKey().getTitle()+" image = "+e.getValue());
        }

        recyclerView = findViewById(R.id.cardView);

        categories = new LinkedList<>();

        categories.add(new Category("business",R.drawable.business));
        categories.add(new Category("entertainment",R.drawable.entertainment));
        categories.add(new Category("general",R.drawable.general));
        categories.add(new Category("health",R.drawable.health));
        categories.add(new Category("science",R.drawable.science));
        categories.add(new Category("sports",R.drawable.sports));
        categories.add(new Category("technology",R.drawable.technology));
        categories.add(new Category("search",R.drawable.search));

        catrecycler = findViewById(R.id.gridView);
        categoryAdapter = new CategoryAdapter(categories,getApplicationContext());

        //add this line
        categoryAdapter.setRecyclerClickListener(this);


        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),2);
        manager.setOrientation(RecyclerView.VERTICAL);
        catrecycler.setLayoutManager(manager);
        catrecycler.setAdapter(categoryAdapter);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/").addConverterFactory(GsonConverterFactory.create()).build();
        interf = retrofit.create(RetrofitUtilsInterface.class);

        ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1234);

        internetBroadcastReceiver = new InternetBroadcastReceiver() {
            @Override
            public void onNetworkStop() {
                internetStopDialog();
            }

            @Override
            public void onNetworkOn() {
                getNewsData();
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1234) {
            getNewsData();
        }
    }

    public void getNewsData()
    {
        //add this condition
        if(!InternetConnectivity.isNetworkConnected(getApplicationContext()) && OfflineNews.isOffline_news()!=true)
        {
            Call<News> data = interf.getBreakingNews("in", NewsApiKey.getApi_key());

            data.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (!response.isSuccessful()) {
                        Log.d("retroFit", response.code() + "");
                        return;
                    }

                    News news = response.body();
                    ArrayList<Articles> articles = news.getArticles();

                    adapter = new BreakingNewsAdapter(articles, getApplicationContext());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    new DBManager(getApplicationContext()).insertNews(articles,"breaking-news");
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Log.d("retroFit", t.getMessage());
                }
            });
        }
        else if(OfflineNews.isOffline_news())
        {
            Log.d("offlineNews","Offline Data Loaded");
            LinkedHashMap<Articles,String> newsData = new DBManager(getApplicationContext()).readNews("breaking-news");

            ArrayList<LinkedHashMap<Articles,String>> nd = new ArrayList<>();

            for(Map.Entry<Articles,String> e:newsData.entrySet())
            {
                Articles a = e.getKey();
                String i = e.getValue();

                LinkedHashMap<Articles,String> n = new LinkedHashMap<>();
                n.put(a,i);

                nd.add(n);
            }

            OfflineBreakingNewsAdapter offlineBreakingNewsAdapter = new OfflineBreakingNewsAdapter(nd,getApplicationContext());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
            gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(offlineBreakingNewsAdapter);

            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            internetStopDialog();
        }
    }

    @Override
    public void onClicked(int position)
    {
        String category = categories.get(position).getName();
        if(category.equals("search")) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(internetBroadcastReceiver,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(internetBroadcastReceiver);
    }

    public void internetStopDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Internet connectivity is required for app functioning ! ");
        builder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialog.dismiss();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(Build.VERSION.SDK_INT > 15)
                {
                    Intent intent = new Intent("android.settings.NETWORK_OPERATOR_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
                    intent.setComponent(cName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    startActivity(intent);
                }
            }
        });
        builder.setNeutralButton("Offline",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Log.d("offlineNews","Offline Selected");
                dialog.dismiss();
                OfflineNews.setOffline_news(true);
                getNewsData();
            }
        });
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
}
