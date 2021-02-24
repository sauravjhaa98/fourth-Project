package com.example.dailynews.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailynews.R;
import com.example.dailynews.connectivity.OfflineNews;
import com.example.dailynews.webview.WebViewActivity;
import com.squareup.picasso.Picasso;

public class NewsFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_layout,container,false);

        Bundle bundle = getArguments();
        String image = bundle.getString("image");
        String title = bundle.getString("title");
        String description = bundle.getString("description");
        String link = bundle.getString("link");
        int defaultImage = bundle.getInt("defaultImage");

        ImageView news_image = view.findViewById(R.id.news_image);
        TextView news_title = view.findViewById(R.id.news_title);
        TextView news_description = view.findViewById(R.id.news_description);
        TextView read_more = view.findViewById(R.id.read_more);

        read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("link",link);
                startActivity(intent);
            }
        });
        if(!OfflineNews.isOffline_news())
        Picasso.get().load(image).placeholder(defaultImage).into(news_image);
        else
        {
            Log.d("offlineNewsFragment","Offline Image loaded");
            String il = Environment.getExternalStoragePublicDirectory("DailyNews").getAbsolutePath()+"/"+image;
            news_image.setImageURI(Uri.parse(il));
        }
        news_title.setText(title);
        news_description.setText(description);

        return view;
    }
}
