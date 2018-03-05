package com.alks_ander.higister.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import com.alks_ander.higister.data.model.LastFM.LastFmResponse;

public interface MusicsService {

    String ENDPOINT = "http://ws.audioscrobbler.com/2.0/";

    @GET("?api_key=de7f4ddaf4e34d29b983717b53f737b2&method=track.search&format=json")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<LastFmResponse> fetchMusics(
            @Query("track") String t
    );

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static MusicsService newMusicsService() {

            return new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build()
                    .create(MusicsService.class);
        }
    }



}
