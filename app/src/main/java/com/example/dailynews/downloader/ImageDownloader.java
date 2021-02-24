package com.example.dailynews.downloader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;
import com.example.dailynews.model.DBManager;

import java.io.File;
import java.util.UUID;

public class ImageDownloader
{
    Context context;

    public ImageDownloader(Context context) {
        this.context = context;
        PRDownloader.initialize(context);
    }

    public void downloadImage(String url)
    {
        File file = Environment.getExternalStoragePublicDirectory("DailyNews");

        if(!file.exists())
            file.mkdir();

        String imageName = UUID.randomUUID().toString().replaceAll("-","")+".jpg";

        DownloadRequest download = PRDownloader.download(url,file.getAbsolutePath(),imageName).build();

        download.start(new OnDownloadListener()
        {
            @Override
            public void onDownloadComplete()
            {
                Log.d("downloadImage","complete "+imageName);
                new DBManager(context).updateImage(url,imageName);
            }

            @Override
            public void onError(Error error) {
                Log.d("downloadImage","error "+imageName);
            }
        });
    }
}
