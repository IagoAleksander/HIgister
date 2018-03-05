package com.alks_ander.higister.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import com.alks_ander.higister.data.model.Omdb.OmdbResponse;

public interface MoviesService {

    String ENDPOINT = "http://www.omdbapi.com";

    @GET("/?&apikey=16a9d413")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<OmdbResponse> fetchMovies(
            @Query("type") String t,
            @Query("s") String s
    );

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static MoviesService newMoviesService() {

            return new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build()
                    .create(MoviesService.class);
        }
    }



}
