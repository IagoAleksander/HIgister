package com.iaz.HIgister.data;


import com.iaz.HIgister.data.model.BaseItem;
import com.iaz.HIgister.data.model.ComicVine.ComicVineResponse;
import com.iaz.HIgister.data.model.ComicVine.Results;
import com.iaz.HIgister.data.model.GoodReads.search.GoodreadsResponse;
import com.iaz.HIgister.data.model.GoodReads.search.Work;
import com.iaz.HIgister.data.model.LastFM.LastFmResponse;
import com.iaz.HIgister.data.model.LastFM.Track;
import com.iaz.HIgister.data.model.MyAnimeList.MyAnimeListResponse;
import com.iaz.HIgister.data.model.MyAnimeList.Result;
import com.iaz.HIgister.data.model.Omdb.movieDetails.MovieDetails;
import com.iaz.HIgister.data.model.Omdb.search.OmdbResponse;
import com.iaz.HIgister.data.model.Omdb.search.Search;
import com.iaz.HIgister.data.remote.AnimesService;
import com.iaz.HIgister.data.remote.BooksService;
import com.iaz.HIgister.data.remote.ComicsService;
import com.iaz.HIgister.data.remote.MoviesService;
import com.iaz.HIgister.data.remote.MusicsService;
import com.iaz.HIgister.ui.search.RecyclerViewFragment2;
import com.iaz.HIgister.util.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.iaz.HIgister.util.Constants.BOOKS;


/**
 * Created by alksander on 01/03/2018.
 */

public class BackendManager {

    public void fetchMovies(final RecyclerViewFragment2 activity, int type, String text) {

        String typeString;

        if (type == Constants.MOVIES) {
            typeString = "movie";
        } else {
            typeString = "series";
        }

        MoviesService.Creator.newMoviesService().fetchMovies(typeString, text).enqueue(
                new Callback<OmdbResponse>() {
                    @Override
                    public void onResponse(Call<OmdbResponse> call, Response<OmdbResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body().getSearch() != null) {

                            for (Search search : response.body().getSearch())
                                results.add(new BaseItem(search, type));
                        }

                        activity.showItems(results, type);
                    }

                    @Override
                    public void onFailure(Call<OmdbResponse> call, Throwable t) {
                        Timber.d(t.toString());
                        activity.showItems(new ArrayList<>(), type);
                    }
                }

        );
    }

    public void fetchMovieDetails(String id, int type, OnUpdateResult onUpdateResult) {

        MoviesService.Creator.newMoviesService().fetchMovieDetails(id).enqueue(
                new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                        onUpdateResult.onSuccess(new BaseItem(response.body(), type));
                    }

                    @Override
                    public void onFailure(Call<MovieDetails> call, Throwable t) {
                        Timber.d(t.toString());
                        onUpdateResult.onFailed(t.getMessage());
                    }
                }

        );
    }

    public void fetchAnimes(final RecyclerViewFragment2 activity, int type, String text) {

        String typeString;

        if (type == Constants.ANIMES) {
            typeString = "anime";
        } else {
            typeString = "manga";
        }

        AnimesService.Creator.newAnimesService().fetchAnimes(typeString, text).enqueue(
                new Callback<MyAnimeListResponse>() {
                    @Override
                    public void onResponse(Call<MyAnimeListResponse> call, Response<MyAnimeListResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResult().size() > 10)
                                for (Result result : response.body().getResult().subList(0, 10))
                                    results.add(new BaseItem(result, type));
                            else
                                for (Result result : response.body().getResult())
                                    results.add(new BaseItem(result, type));

                        }
                        activity.showItems(results, type);
                    }

                    @Override
                    public void onFailure(Call<MyAnimeListResponse> call, Throwable t) {
                        Timber.d(t.toString());
                        activity.showItems(new ArrayList<>(), type);
                    }
                }

        );
    }

    public void fetchBooks(final RecyclerViewFragment2 activity, String text) {

        BooksService.Creator.newBooksService().fetchBooks(text).enqueue(
                new Callback<GoodreadsResponse>() {
                    @Override
                    public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getSearch().getResults().getWork().size() > 10) {
                                for (Work work : response.body().getSearch().getResults().getWork().subList(0, 10))
                                    results.add(new BaseItem(work.getBest_book()));
                            } else {
                                for (Work work : response.body().getSearch().getResults().getWork())
                                    results.add(new BaseItem(work.getBest_book()));
                            }
                        }

                        activity.showItems(results, BOOKS);
                    }

                    @Override
                    public void onFailure(Call<GoodreadsResponse> call, Throwable t) {
                        Timber.d(t.toString());
                        activity.showItems(new ArrayList<BaseItem>(), BOOKS);
                    }
                }

        );
    }

    public void fetchBookDetails(String id, OnUpdateResult onUpdateResult) {

        int idNumber = Integer.parseInt(id);

        BooksService.Creator.newBookDetailsService().fetchBookDetails(idNumber).enqueue(
                new Callback<com.iaz.HIgister.data.model.GoodReads.bookDetails.GoodreadsResponse>() {
                    @Override
                    public void onResponse(Call<com.iaz.HIgister.data.model.GoodReads.bookDetails.GoodreadsResponse> call, Response<com.iaz.HIgister.data.model.GoodReads.bookDetails.GoodreadsResponse> response) {
                        onUpdateResult.onSuccess(new BaseItem(response.body().getBook()));
                    }

                    @Override
                    public void onFailure(Call<com.iaz.HIgister.data.model.GoodReads.bookDetails.GoodreadsResponse> call, Throwable t) {
                        Timber.d(t.toString());
                        onUpdateResult.onFailed(t.getMessage());
                    }
                }

        );
    }

    public void fetchMusics(final RecyclerViewFragment2 activity, String text) {

        MusicsService.Creator.newMusicsService().fetchMusics(text).enqueue(
                new Callback<LastFmResponse>() {
                    @Override
                    public void onResponse(Call<LastFmResponse> call, Response<LastFmResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResults().getTrackmatches().getTrack().size() > 10) {
                                for (Track track : response.body().getResults().getTrackmatches().getTrack().subList(0, 10))
                                    results.add(new BaseItem(track));
                            } else {
                                for (Track track : response.body().getResults().getTrackmatches().getTrack())
                                    results.add(new BaseItem(track));
                            }
                        }

                        activity.showItems(results, Constants.MUSICS);
                    }

                    @Override
                    public void onFailure(Call<LastFmResponse> call, Throwable t) {
                        Timber.d(t.toString());
                        activity.showItems(new ArrayList<BaseItem>(), Constants.MUSICS);
                    }
                }

        );
    }

    public void fetchComics(final RecyclerViewFragment2 activity, String text) {

        ComicsService.Creator.newComicsService().fetchComics(text).enqueue(
                new Callback<ComicVineResponse>() {
                    @Override
                    public void onResponse(Call<ComicVineResponse> call, Response<ComicVineResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResults().size() > 10) {
                                for (Results result : response.body().getResults().subList(0, 10))
                                    results.add(new BaseItem(result));
                            } else {
                                for (Results result : response.body().getResults())
                                    results.add(new BaseItem(result));
                            }
                        }

                        activity.showItems(results, Constants.COMICS);
                    }

                    @Override
                    public void onFailure(Call<ComicVineResponse> call, Throwable t) {
                        Timber.d(t.toString());
                        activity.showItems(new ArrayList<BaseItem>(), Constants.COMICS);
                    }
                }

        );
    }

    public interface OnUpdateResult {
        void onSuccess(BaseItem baseItem);

        void onFailed(String e);
    }
}
