package com.example.dailynews.connectivity;

public class OfflineNews
{
    private static boolean offline_news;

    public static boolean isOffline_news() {
        return offline_news;
    }

    public static void setOffline_news(boolean offline_news) {
        OfflineNews.offline_news = offline_news;
    }
}
