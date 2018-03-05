package com.alks_ander.higister.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import com.alks_ander.higister.data.model.MyAnimeList.MyAnimeListResponse;

public interface AnimesService {

    String ENDPOINT = "http://api.jikan.me/";

    @GET("/search/{type}/{s}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<MyAnimeListResponse> fetchAnimes(
            @Path("type") String t,
            @Path("s") String s
    );

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static AnimesService newAnimesService() {

            return new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build()
                    .create(AnimesService.class);
        }
    }



}
