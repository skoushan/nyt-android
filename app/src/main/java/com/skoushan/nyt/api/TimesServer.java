package com.skoushan.nyt.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.skoushan.nyt.models.TimesSearchArticle;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

public class TimesServer {
    private static TimesServerInterface server;

    public static TimesServerInterface get() {
        if (server == null) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(Response.class, new TimesSearchDeserializer())
                            .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.nytimes.com")
                    .setConverter(new GsonConverter(gson))
                    .build();

            server = restAdapter.create(TimesServerInterface.class);
        }
        return server;
    }

    public interface TimesServerInterface {
        @Headers("Accept: application/json")
        @GET("/svc/search/v2/articlesearch.json?api-key=5837f028db00e46daeaa2f8b41080f2a%3A3%3A69972922")
        void search(@Query("q") String query, Callback<Response> cb);
    }

    public class Response {
        public List<TimesSearchArticle> docs;
    }

    public static class TimesSearchDeserializer implements JsonDeserializer<Response> {
        @Override
        public Response deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException {
            // Get the "content" element from the parsed JSON
            JsonElement content = je.getAsJsonObject().get("response").getAsJsonObject();

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(content, Response.class);

        }
    }
}
