package com.example.dailynews.jsondata;

import java.util.ArrayList;

public class News
{
    private String status;
    private int totalResult;
    private ArrayList<Articles> articles;

    public String getStatus() {
        return status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }
}
