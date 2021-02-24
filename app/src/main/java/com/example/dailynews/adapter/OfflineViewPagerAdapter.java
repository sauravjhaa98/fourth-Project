package com.example.dailynews.adapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dailynews.R;
import com.example.dailynews.fragment.NewsFragment;
import com.example.dailynews.jsondata.Articles;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OfflineViewPagerAdapter extends FragmentStateAdapter
{
    ArrayList<LinkedHashMap<Articles,String>> data;
    String category;

    public OfflineViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<LinkedHashMap<Articles,String>> data, String category)
    {
        super(fragmentManager, lifecycle);
        this.data = data;
        this.category = category;
        Log.d("offlineNewsFragment","data size "+this.data.size());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        Log.d("offlineNewsFragment","fragment created");
        LinkedHashMap<Articles,String> n = data.get(position);
        Articles a = null;
        String s = null;
        for(Map.Entry<Articles,String> e:n.entrySet())
        {
            a = e.getKey();
            s = e.getValue();
        }

        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image",s);
        bundle.putString("title",a.getTitle());
        bundle.putString("description",a.getDescription());
        bundle.putString("link",a.getUrl());
        switch (category)
        {
            case "business":
                bundle.putInt("defaultImage", R.drawable.business_news);
                break;
            case "entertainment":
                bundle.putInt("defaultImage",R.drawable.entertainment1);
                break;
            case "general":
                bundle.putInt("defaultImage",R.drawable.general1);
                break;
            case "health":
                bundle.putInt("defaultImage",R.drawable.health1);
                break;
            case "science":
                bundle.putInt("defaultImage",R.drawable.science1);
                break;
            case "sports":
                bundle.putInt("defaultImage",R.drawable.sports1);
                break;
            case "technology":
                bundle.putInt("defaultImage",R.drawable.technology1);
                break;

        }
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

