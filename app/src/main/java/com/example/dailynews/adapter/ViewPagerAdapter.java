package com.example.dailynews.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dailynews.R;
import com.example.dailynews.fragment.NewsFragment;
import com.example.dailynews.jsondata.Articles;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter
{
    ArrayList<Articles> data;
    String category;
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Articles> data,String category)
    {
        super(fragmentManager, lifecycle);
        this.data = data;
        this.category = category;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image",data.get(position).getUrlToImage());
        bundle.putString("title",data.get(position).getTitle());
        bundle.putString("description",data.get(position).getDescription());
        bundle.putString("link",data.get(position).getUrl());
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
