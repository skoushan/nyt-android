package com.skoushan.nyt;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;

public interface ServerService {

    @Headers("Accept: application/json")
    @GET("/articles")
    void listArticles(Callback<List<Article>> cb);
}
