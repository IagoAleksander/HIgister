package com.iaz.HIgister.data.remote;


import com.iaz.HIgister.data.model.Omdb.movieDetails.MovieDetails;
import com.iaz.HIgister.data.model.Omdb.search.OmdbResponse;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

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

    @GET("/?&apikey=16a9d413&plot=full")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<MovieDetails> fetchMovieDetails(
            @Query("i") String s
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
