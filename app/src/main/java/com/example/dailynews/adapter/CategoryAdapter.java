package com.example.dailynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailynews.listener.RecyclerClickListener;
import com.example.dailynews.pojo.Category;
import com.example.dailynews.R;

import java.util.LinkedList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.myViewHolder>
{
    LinkedList<Category> categories;
    LayoutInflater inflater;
    Context context;
    RecyclerClickListener recyclerClickListener;//points to homeactivity

    public void setRecyclerClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public CategoryAdapter(LinkedList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.categories_layout,null);
        CategoryAdapter.myViewHolder viewHolder = new CategoryAdapter.myViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerClickListener.onClicked(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.category_image.setImageResource(categories.get(position).getImage());
        String category = categories.get(position).getName();
        char capital = category.charAt(0);
        capital = Character.toUpperCase(capital);
        category = category.replaceFirst(category.charAt(0)+"",capital+"");
        holder.category.setText(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView category_image;
        TextView category;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            category_image = itemView.findViewById(R.id.category_image);
            category = itemView.findViewById(R.id.category);
        }
    }
}
