package com.example.dailynews.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailynews.R;
import com.example.dailynews.jsondata.Articles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OfflineBreakingNewsAdapter extends RecyclerView.Adapter<OfflineBreakingNewsAdapter.myViewHolder>
{
    ArrayList<LinkedHashMap<Articles,String>> articles;
    LayoutInflater inflater;
    Context context;

    public OfflineBreakingNewsAdapter(ArrayList<LinkedHashMap<Articles,String>> articles, Context context)
    {
        this.articles = articles;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public OfflineBreakingNewsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.cardview_layout,null);
        OfflineBreakingNewsAdapter.myViewHolder viewHolder = new OfflineBreakingNewsAdapter.myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineBreakingNewsAdapter.myViewHolder holder, int position)
    {
        LinkedHashMap<Articles,String> data = articles.get(position);
        Articles a=null;
        String i=null;
        for(Map.Entry<Articles,String> e:data.entrySet())
        {
            a = e.getKey();
            i = e.getValue();
        }
        holder.news_title.setText(a.getTitle());
        String image_path = Environment.getExternalStoragePublicDirectory("DailyNews").getAbsolutePath()+"/"+i;
        holder.news_image.setImageURI(Uri.parse(image_path));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView news_image;
        TextView news_title;
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            news_image = itemView.findViewById(R.id.news_image);
            news_title = itemView.findViewById(R.id.news_title);
        }
    }
}
