package com.example.dailynews.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;

import android.app.job.JobService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dailynews.R;
import com.example.dailynews.jsondata.Articles;
import com.example.dailynews.model.DBManager;
import com.example.dailynews.views.NewsActivity;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobScheduler extends JobService
{
    int id=0;
    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        DBManager dbManager = new DBManager(getApplicationContext());
        LinkedHashMap<Articles,String> sports = dbManager.readNews("sports");
        getDataForNotification(sports);
        LinkedHashMap<Articles,String> business = dbManager.readNews("business");
        getDataForNotification(business);
        LinkedHashMap<Articles,String> technology = dbManager.readNews("technology");
        getDataForNotification(technology);
        LinkedHashMap<Articles,String> entertainment = dbManager.readNews("entertainment");
        getDataForNotification(entertainment);
        LinkedHashMap<Articles,String> general = dbManager.readNews("general");
        getDataForNotification(general);
        LinkedHashMap<Articles,String> science = dbManager.readNews("science");
        getDataForNotification(science);
        LinkedHashMap<Articles,String> health = dbManager.readNews("health");
        getDataForNotification(health);
        return true;
    }

    public void getDataForNotification(LinkedHashMap<Articles,String> data)
    {
        Articles article = null;
        String image = null;

        for(Map.Entry<Articles,String> e:data.entrySet())
        {
            article = e.getKey();
            image = e.getValue();
        }
        if(article!=null)
        {
            displayNotification(article,image);
        }
    }
    public void displayNotification(Articles articles,String image)
    {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("news","news", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),null);
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"news");
        builder.setSmallIcon(R.drawable.newsapplogo);
        builder.setContentText(articles.getTitle());
        String link = Environment.getExternalStoragePublicDirectory("DailyNews").getAbsolutePath()+"/"+image;
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle().bigPicture
                (BitmapFactory.decodeFile(link));
        builder.setStyle(bigPictureStyle);

        Notification notification = builder.build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(id,notification);
        id++;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
