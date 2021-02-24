package com.example.dailynews.adapter;

import android.content.Context;
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

public class BreakingNewsAdapter extends RecyclerView.Adapter<BreakingNewsAdapter.myViewHolder>
{
    ArrayList<Articles> articles;
    LayoutInflater inflater;
    Context context;

    public BreakingNewsAdapter(ArrayList<Articles> articles, Context context)
    {
        this.articles = articles;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.cardview_layout,null);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.news_title.setText(articles.get(position).getTitle());
        ///////////////////////////////////////////////////
        Picasso.get().load(articles.get(position).getUrlToImage()).
                placeholder(context.getResources().getDrawable(R.drawable.breaking_news)).into(holder.news_image);
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
