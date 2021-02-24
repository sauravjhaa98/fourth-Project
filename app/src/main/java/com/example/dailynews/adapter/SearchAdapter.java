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
import com.example.dailynews.listener.RecyclerClickListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.myViewHolder>
{
    ArrayList<Articles> articles;
    Context context;
    LayoutInflater inflater;
    RecyclerClickListener recyclerClickListener;//searchactivity

    public void setRecyclerClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public SearchAdapter(ArrayList<Articles> articles, Context context) {
        this.articles = articles;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_layout,null);
        myViewHolder myViewHolder = new myViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerClickListener.onClicked(myViewHolder.getAdapterPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.textView.setText(articles.get(position).getTitle());
        Picasso.get().load(articles.get(position).getUrlToImage()).placeholder(R.drawable.newsapplogo).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_news_imag);
            textView = itemView.findViewById(R.id.search_news_title);
        }
    }
}
