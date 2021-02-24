package com.example.dailynews.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.dailynews.downloader.ImageDownloader;
import com.example.dailynews.jsondata.Articles;
import com.example.dailynews.jsondata.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DBManager
{
    dbHelper helper;
    Context context;
    public DBManager(Context context)
    {
        helper = new dbHelper(context);
        this.context = context;
    }

    public LinkedHashMap<Articles,String> readNews(String category)
    {
        LinkedHashMap<Articles,String> articles = new LinkedHashMap<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String columns[] = {dbHelper.SOURCE_ID,dbHelper.SOURCE_NAME,dbHelper.AUTHOR,dbHelper.TITLE,dbHelper.DESCRIPTION,dbHelper.URL,
                dbHelper.URL_TO_IMAGE,dbHelper.PUBLISHED_AT,dbHelper.CONTENT,dbHelper.IMAGE_FILE,dbHelper.NEWS_CATEGORY};
        String args[] = {category};
        Cursor cursor = db.query(dbHelper.TABLE_ARTICLES,columns,dbHelper.NEWS_CATEGORY+" = ?",args,null,null,null);
        while(cursor.moveToNext())
        {
            String source_id = cursor.getString(cursor.getColumnIndex(dbHelper.SOURCE_ID));
            String source_name = cursor.getString(cursor.getColumnIndex(dbHelper.SOURCE_NAME));
            String author = cursor.getString(cursor.getColumnIndex(dbHelper.AUTHOR));
            String title = cursor.getString(cursor.getColumnIndex(dbHelper.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(dbHelper.DESCRIPTION));
            String url = cursor.getString(cursor.getColumnIndex(dbHelper.URL));
            String url_to_image = cursor.getString(cursor.getColumnIndex(dbHelper.URL_TO_IMAGE));
            String published = cursor.getString(cursor.getColumnIndex(dbHelper.PUBLISHED_AT));
            String content = cursor.getString(cursor.getColumnIndex(dbHelper.CONTENT));
            String image_file = cursor.getString(cursor.getColumnIndex(dbHelper.IMAGE_FILE));

            Articles a = new Articles();
            Source s = new Source();
            s.setId(source_id);
            s.setName(source_name);
            a.setAuthor(author);
            a.setTitle(title);
            a.setDescription(description);
            a.setUrl(url);
            a.setUrlToImage(url_to_image);
            a.setPublishedAt(published);
            a.setContent(content);
            articles.put(a,image_file);
        }
        return articles;
    }

    public void insertNews(ArrayList<Articles> news,String category)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        for(Articles a:news)
        {
            ContentValues values = new ContentValues();
            values.put(helper.SOURCE_ID,a.getSource().getId());
            values.put(helper.SOURCE_NAME,a.getSource().getName());
            values.put(helper.AUTHOR,a.getAuthor());
            values.put(helper.TITLE,a.getTitle());
            values.put(helper.DESCRIPTION,a.getDescription());
            values.put(helper.URL,a.getUrl());
            values.put(helper.URL_TO_IMAGE,a.getUrlToImage());
            values.put(helper.PUBLISHED_AT,a.getPublishedAt());
            values.put(helper.CONTENT,a.getContent());
            values.put(helper.IMAGE_FILE,"null");
            values.put(helper.NEWS_CATEGORY,category);

            long result = db.insert(helper.TABLE_ARTICLES,null,values);

            if(result!=-1)
            {//now download image
                new ImageDownloader(context).downloadImage(a.getUrlToImage());
            }
        }
    }

    public void updateImage(String url,String image_file)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.IMAGE_FILE,image_file);

        String args[] = {url};

        db.update(helper.TABLE_ARTICLES,values,helper.URL_TO_IMAGE+" = ?",args);
    }

    class dbHelper extends SQLiteOpenHelper
    {

        public static final String DATABASE_NAME = "NewsApp";
        public static final int DATABASE_VERSION = 1;

        public static final String TABLE_ARTICLES = "articles";

        public static final String SOURCE_ID = "source_id";
        public static final String SOURCE_NAME = "source_name" ;
        public static final String AUTHOR = "author";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "desciption";
        public static final String URL = "url";
        public static final String URL_TO_IMAGE = "url_to_image";
        public static final String PUBLISHED_AT = "published_at";
        public static final String CONTENT = "content";
        public static final String IMAGE_FILE = "image";
        //add new column
        public static final String NEWS_CATEGORY = "category";

        //UNIQUE -> we can only insert unique value in that column if we insert duplicate we get an error


        public static final String CREATE_TABLE_ARTICLES = "CREATE TABLE "
                +TABLE_ARTICLES+" ( "+SOURCE_ID+" TEXT , "+SOURCE_NAME+
                " TEXT , "+AUTHOR+" TEXT , "+TITLE+" TEXT UNIQUE , "+DESCRIPTION+
                " TEXT , "+URL+" TEXT , "+URL_TO_IMAGE+" TEXT , "
                +PUBLISHED_AT+" TEXT , "+CONTENT+" TEXT , "+IMAGE_FILE+" TEXT , "+NEWS_CATEGORY+" TEXT )";

        public dbHelper(@Nullable Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_ARTICLES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
