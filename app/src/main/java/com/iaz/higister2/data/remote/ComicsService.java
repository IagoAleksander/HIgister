package com.iaz.higister2.data.remote;

import com.iaz.higister2.data.model.ComicVine.ComicVineResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ComicsService {

    String ENDPOINT = "https://comicvine.gamespot.com/api/search/";

    @GET("?api_key=5d70b01d464609827ecb181639814dabf7d2553e&format=json&sort=name:asc&resources=issue")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<ComicVineResponse> fetchComics(
            @Query("query") String t
    );

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static ComicsService newComicsService() {

            return new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build()
                    .create(ComicsService.class);
        }
    }



}
