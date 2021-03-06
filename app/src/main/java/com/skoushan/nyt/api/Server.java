package com.skoushan.nyt.api;

import com.skoushan.nyt.models.Article;
import com.skoushan.nyt.models.Section;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

public class Server {

    private static ServerInterface server;

    public static ServerInterface get() {
        if (server == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://limitless-tundra-9951.herokuapp.com/api")
//                    .setEndpoint("http://192.168.43.194:3000/api")
                    .build();

            server = restAdapter.create(ServerInterface.class);
        }
        return server;
    }

    public interface ServerInterface {
        @Headers("Accept: application/json")
        @GET("/articles")
        void listArticles(Callback<List<Article>> cb);

        @Headers("Accept: application/json")
        @GET("/articles")
        void listArticles(@Query("section") String section, Callback<List<Article>> cb);

        @Headers("Accept: application/json")
        @GET("/sections?page_size=100")
        void listSections(Callback<List<Section>> cb);
    }
}
