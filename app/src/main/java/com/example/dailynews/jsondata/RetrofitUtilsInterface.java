package com.example.dailynews.jsondata;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitUtilsInterface
{
    @GET("v2/top-headlines")
    Call<News> getBreakingNews(@Query("country") String country, @Query("apiKey")String apiKey);

    @GET("v2/top-headlines")
    Call<News> getNewsCategoryWise(@Query("country")String country,@Query("apiKey") String apiKey,@Query("category") String category);

    @GET("v2/top-headlines")
    Call<News> searchNews(@Query("apiKey")String apiKey,@Query("q")String q);
}
